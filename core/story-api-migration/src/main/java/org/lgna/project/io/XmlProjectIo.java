/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may
 *    "Alice" appear in their name, without prior written permission of
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is
 *    contributed by Electronic Arts Inc. and may be used for personal,
 *    non-commercial, and academic use only. Redistributions of any program
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/
package org.lgna.project.io;

import edu.cmu.cs.dennisc.codec.InputStreamBinaryDecoder;
import edu.cmu.cs.dennisc.codec.OutputStreamBinaryEncoder;
import edu.cmu.cs.dennisc.java.io.InputStreamUtilities;
import edu.cmu.cs.dennisc.java.io.TextFileUtilities;
import edu.cmu.cs.dennisc.java.lang.ClassUtilities;
import edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities;
import edu.cmu.cs.dennisc.java.util.zip.ByteArrayDataSource;
import edu.cmu.cs.dennisc.java.util.zip.DataSource;
import edu.cmu.cs.dennisc.java.util.zip.ZipUtilities;
import edu.cmu.cs.dennisc.pattern.IsInstanceCrawler;
import edu.cmu.cs.dennisc.print.PrintUtilities;
import edu.cmu.cs.dennisc.xml.XMLUtilities;
import org.alice.serialization.xml.XmlEncoderDecoder;
import org.lgna.common.Resource;
import org.lgna.project.Project;
import org.lgna.project.ProjectVersion;
import org.lgna.project.Version;
import org.lgna.project.VersionNotSupportedException;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.CrawlPolicy;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.ResourceExpression;
import org.lgna.project.migration.MigrationManager;
import org.lgna.project.migration.ProjectMigrationManager;
import org.lgna.project.migration.ast.AstMigrationUtilities;
import org.lgna.project.properties.PropertyKey;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.zip.ZipOutputStream;

public class XmlProjectIo implements ProjectIo{

	private static final String PROGRAM_TYPE_ENTRY_NAME = "programType.xml";
	private static final String TYPE_ENTRY_NAME = "type.xml";
	private static final String RESOURCES_ENTRY_NAME = "resources.xml";

	private static final String XML_RESOURCE_TAG_NAME = "resource";

	private static final String XML_RESOURCE_CLASSNAME_ATTRIBUTE = "className";
	private static final String XML_RESOURCE_UUID_ATTRIBUTE = "uuid";
	private static final String XML_RESOURCE_ENTRY_NAME_ATTRIBUTE = "entryName";


	@Override
	public Project readProject( ZipEntryContainer container )
					throws IOException, VersionNotSupportedException {
		NamedUserType type = readType( container, PROGRAM_TYPE_ENTRY_NAME );
		Set<Resource> resources = readResources( container );

		Set<NamedUserType> namedUserTypes = Collections.emptySet();
		Project project = new Project( type, namedUserTypes, resources );

		readProperties( project, container );
		return project;
	}

	@Override
	public TypeResourcesPair readType( ZipEntryContainer zipEntryContainer ) throws IOException, VersionNotSupportedException {
		NamedUserType type = readType( zipEntryContainer, TYPE_ENTRY_NAME );
		Set<Resource> resources = readResources( zipEntryContainer );
		return new TypeResourcesPair( type, resources );
	}

	private static Version readSourceProgramVersion( ZipEntryContainer zipEntryContainer ) throws IOException {
		assert zipEntryContainer != null;
		InputStream is = zipEntryContainer.getInputStream( VERSION_ENTRY_NAME );
		if( is != null ) {
			ArrayList<Byte> buffer = new ArrayList<>( 32 );
			while( true ) {
				int b = is.read();
				if( b != -1 ) {
					buffer.add( (byte)b );
				} else {
					break;
				}
			}
			byte[] array = new byte[ buffer.size() ];
			int i = 0;
			for( Byte b : buffer ) {
				array[ i++ ] = b;
			}
			return new Version( new String( array ) );
		} else {
			throw new IOException( zipEntryContainer.toString() + " does not contain entry " + VERSION_ENTRY_NAME );
		}
	}

	private static void readProperties( Project project, ZipEntryContainer zipEntryContainer ) throws IOException {
		InputStream is = zipEntryContainer.getInputStream( PROPERTIES_ENTRY_NAME );
		if( is != null ) {
			BufferedInputStream bis = new BufferedInputStream( is );
			InputStreamBinaryDecoder binaryDecoder = new InputStreamBinaryDecoder( bis );
			String version = binaryDecoder.decodeString();
			final int N = binaryDecoder.decodeInt();
			for( int i = 0; i < N; i++ ) {
				PropertyKey.decodeIdAndValueAndPut( project, binaryDecoder, version );
			}
		}
	}

	private static Document readXML( InputStream is, MigrationManagerDecodedVersionPair[] migrationpairs ) throws IOException {
		String modifiedText = null;
		for( MigrationManagerDecodedVersionPair migrationpair : migrationpairs ) {
			MigrationManager migrationManager = migrationpair.getMigrationManager();
			Version decodedVersion = migrationpair.getDecodedVersion();
			if ((migrationManager.getCurrentVersion().compareTo( decodedVersion ) != 0) || !migrationManager
							.isDevoidOfVersionIndependentMigrations()) {
				if (modifiedText == null) {
					modifiedText = TextFileUtilities.read( new InputStreamReader( is, "UTF-8" ) );
				}
				modifiedText = migrationManager.migrate( modifiedText, decodedVersion );
			}
		}
		if( modifiedText != null ) {
			is = new ByteArrayInputStream( modifiedText.getBytes( "UTF-8" ) );
		}
		return XMLUtilities.read( is );
	}

	private static Document readXML( ZipEntryContainer zipEntryContainer, String entryName, MigrationManagerDecodedVersionPair[] migrationManagerDecodedVersionPairs ) throws IOException {
		assert zipEntryContainer != null;
		InputStream is = zipEntryContainer.getInputStream( entryName );
		return readXML( is, migrationManagerDecodedVersionPairs );
	}

	private static NamedUserType readType( ZipEntryContainer zipEntryContainer, String entryName) throws IOException, VersionNotSupportedException {
		Version decodedProjectVersion = readSourceProgramVersion( zipEntryContainer );

		MigrationManagerDecodedVersionPair[] migrationManagerDecodedVersionPairs = {
				new MigrationManagerDecodedVersionPair( ProjectMigrationManager.getInstance(), decodedProjectVersion )
		};

		Document xmlDocument = readXML( zipEntryContainer, entryName, migrationManagerDecodedVersionPairs );
		NamedUserType type = (NamedUserType) (new XmlEncoderDecoder()).decode( xmlDocument );

		AstMigrationUtilities.migrateNode( type, null, migrationManagerDecodedVersionPairs );
		return type;
	}

	private static Set<Resource> readResources( ZipEntryContainer zipEntryContainer ) throws IOException {
		Set<Resource> resources = new HashSet<>();
		InputStream isResources = zipEntryContainer.getInputStream( RESOURCES_ENTRY_NAME );
		if( isResources != null ) {
			Document xmlDocument = XMLUtilities.read( isResources );
			List<Element> xmlElements = XMLUtilities.getChildElementsByTagName( xmlDocument.getDocumentElement(), XML_RESOURCE_TAG_NAME );
			for( Element xmlElement : xmlElements ) {
				String className = xmlElement.getAttribute( XML_RESOURCE_CLASSNAME_ATTRIBUTE );
				String uuidText = xmlElement.getAttribute( XML_RESOURCE_UUID_ATTRIBUTE );
				String entryName = xmlElement.getAttribute( XML_RESOURCE_ENTRY_NAME_ATTRIBUTE );
				if( ( className != null ) && ( uuidText != null ) && ( entryName != null ) ) {
					byte[] data = InputStreamUtilities.getBytes( zipEntryContainer.getInputStream( entryName ) );
					if( data != null ) {
						try {
							Class<? extends Resource> resourceCls = (Class<? extends Resource>) ClassUtilities.forName( className );
							Resource resource = ReflectionUtilities.valueOf( resourceCls, uuidText );
							resource.decodeAttributes( xmlElement, data );
							resources.add( resource );
						} catch( ClassNotFoundException cnfe ) {
							PrintUtilities.println( "WARNING: no class for name:", className );
						}
					} else {
						PrintUtilities.println( "WARNING: no data for resource:", entryName );
					}
				}
			}
		}
		return resources;
	}

	private static void writeVersion( ZipOutputStream zos, final String entryName, final Version version ) throws IOException {
		ZipUtilities.write( zos, new DataSource() {
			@Override
			public String getName() {
				return entryName;
			}

			@Override
			public void write( OutputStream os ) throws IOException {
				os.write( version.toString().getBytes() );
			}
		} );
	}

	private static void writeVersions( ZipOutputStream zos ) throws IOException {
		writeVersion( zos, VERSION_ENTRY_NAME, ProjectVersion.getCurrentVersion() );
	}

	private static void writeXML( final Document xmlDocument, ZipOutputStream zos, final String entryName ) throws IOException {
		ZipUtilities.write( zos, new DataSource() {
			@Override
			public String getName() {
				return entryName;
			}

			@Override
			public void write( OutputStream os ) {
				XMLUtilities.write( xmlDocument, os );
			}
		} );
	}

	private static void writeType( NamedUserType type, ZipOutputStream zos, String entryName ) throws IOException {
		writeXML( (new XmlEncoderDecoder()).encode(type), zos, entryName );
	}

	private static void writeDataSources( ZipOutputStream zos, DataSource... dataSources ) throws IOException {
		for( DataSource dataSource : dataSources ) {
			ZipUtilities.write( zos, dataSource );
		}
	}

	private static String getValidName( String name ) {
		//todo
		return name;
	}

	private static String generateEntryName( Resource resource, Set<String> usedEntryNames ) {
		String validFilename = getValidName( resource.getOriginalFileName() );
		final String DESIRED_DIRECTORY_NAME = "resources";
		int i = 1;
		while( true ) {
			StringBuilder sb = new StringBuilder();
			sb.append( DESIRED_DIRECTORY_NAME );
			if( i > 1 ) {
				sb.append( i );
			}
			sb.append( "/" );
			sb.append( validFilename );
			String potentialEntryName = sb.toString();
			if( usedEntryNames.contains( potentialEntryName ) ) {
				i += 1;
			} else {
				return potentialEntryName;
			}
		}
	}

	private static void writeResources( ZipOutputStream zos, Set<Resource> resources ) throws IOException {
		if (!resources.isEmpty()) {
			Document xmlDocument = XMLUtilities.createDocument();
			Element xmlRootElement = xmlDocument.createElement( "root" );
			xmlDocument.appendChild( xmlRootElement );
			synchronized( resources ) {
				Set<String> usedEntryNames = new HashSet<>();
				for( Resource resource : resources ) {
					Element xmlElement = xmlDocument.createElement( XML_RESOURCE_TAG_NAME );
					resource.encodeAttributes( xmlElement );
					UUID uuid = resource.getId();
					assert uuid != null;

					xmlElement.setAttribute( XML_RESOURCE_CLASSNAME_ATTRIBUTE, resource.getClass().getName() );
					xmlElement.setAttribute( XML_RESOURCE_UUID_ATTRIBUTE, uuid.toString() );

					String entryName = generateEntryName( resource, usedEntryNames );
					usedEntryNames.add( entryName );
					xmlElement.setAttribute( XML_RESOURCE_ENTRY_NAME_ATTRIBUTE, entryName );
					xmlRootElement.appendChild( xmlElement );
				}
			}
			writeXML( xmlDocument, zos, RESOURCES_ENTRY_NAME );
			synchronized( resources ) {
				Set<String> usedEntryNames = new HashSet<>();
				for( Resource resource : resources ) {
					String entryName = generateEntryName( resource, usedEntryNames );
					usedEntryNames.add( entryName );
					ZipUtilities.write( zos, new ByteArrayDataSource( entryName, resource.getData() ) );
				}
			}
		}
	}

	@Override
	public void writeProject( OutputStream os, final Project project, DataSource... dataSources ) throws IOException {
		ZipOutputStream zos = new ZipOutputStream( os );
		writeVersions( zos );
		NamedUserType programType = project.getProgramType();
		writeType( programType, zos, PROGRAM_TYPE_ENTRY_NAME );
		final Set<PropertyKey<Object>> propertyKeys = project.getPropertyKeys();
		if (!propertyKeys.isEmpty()) {
			ZipUtilities.write( zos, new DataSource() {
				@Override
				public String getName() {
					return PROPERTIES_ENTRY_NAME;
				}

				@Override
				public void write( OutputStream os ) {
					OutputStreamBinaryEncoder binaryEncoder = new OutputStreamBinaryEncoder( os );
					binaryEncoder.encode( ProjectVersion.getCurrentVersionText() );
					binaryEncoder.encode( propertyKeys.size() );
					for( PropertyKey<Object> propertyKey : propertyKeys ) {
						propertyKey.encodeIdAndValue( project, binaryEncoder );
					}
					binaryEncoder.flush();
				}
			} );
		}
		writeDataSources( zos, dataSources );
		Set<Resource> resources = project.getResources();

		IsInstanceCrawler<ResourceExpression> crawler = new IsInstanceCrawler<ResourceExpression>( ResourceExpression.class ) {
			@Override
			protected boolean isAcceptable( ResourceExpression resourceExpression ) {
				return true;
			}
		};
		programType.crawl( crawler, CrawlPolicy.COMPLETE );

		for( ResourceExpression resourceExpression : crawler.getList() ) {
			Resource resource = resourceExpression.resource.getValue();
			if (!resources.contains( resource )) {
				PrintUtilities.println( "WARNING: adding missing resource", resource );
				resources.add( resource );
			}
		}

		writeResources( zos, resources );
		zos.flush();
		zos.close();
	}

	@Override
	public void writeType( OutputStream os, NamedUserType type, DataSource... dataSources ) throws IOException {
		ZipOutputStream zos = new ZipOutputStream( os );
		writeVersions( zos );
		writeType( type, zos, TYPE_ENTRY_NAME );
		writeDataSources( zos, dataSources );

		IsInstanceCrawler<ResourceExpression> crawler = new IsInstanceCrawler<ResourceExpression>( ResourceExpression.class ) {
			@Override
			protected boolean isAcceptable( ResourceExpression resourceExpression ) {
				return true;
			}
		};
		type.crawl( crawler, CrawlPolicy.EXCLUDE_REFERENCES_ENTIRELY );
		Set<Resource> resources = new HashSet<>();
		for( ResourceExpression resourceExpression : crawler.getList() ) {
			resources.add( resourceExpression.resource.getValue() );
		}
		writeResources( zos, resources );

		zos.flush();
		zos.close();
	}
}

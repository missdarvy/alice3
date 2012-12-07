/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
 */
package org.alice.media;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.alice.media.YouTubeEvent.EventType;
import org.alice.media.components.UploadView;
import org.lgna.croquet.ActionOperation;
import org.lgna.croquet.BooleanState;
import org.lgna.croquet.ListSelectionState;
import org.lgna.croquet.PlainStringValue;
import org.lgna.croquet.StringState;
import org.lgna.croquet.WizardPageComposite;
import org.lgna.project.Project;

import com.google.gdata.data.media.MediaFileSource;
import com.google.gdata.data.media.mediarss.MediaCategory;
import com.google.gdata.data.media.mediarss.MediaDescription;
import com.google.gdata.data.media.mediarss.MediaKeywords;
import com.google.gdata.data.media.mediarss.MediaTitle;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.YouTubeMediaGroup;
import com.google.gdata.data.youtube.YouTubeNamespace;
import com.google.gdata.util.AuthenticationException;

import edu.cmu.cs.dennisc.java.awt.FileDialogUtilities;
import edu.cmu.cs.dennisc.java.io.FileUtilities;

/**
 * @author Matt May
 */
public class UploadComposite extends WizardPageComposite<UploadView> implements YouTubeListener {

	private final YouTubeUploader uploader = new YouTubeUploader();
	private final ExportToYouTubeWizardDialogComposite owner;
	private final StringState idState = this.createStringState( this.createKey( "id" ), "" );
	private final PlainStringValue username = this.createStringValue( this.createKey( "username" ) );
	private final StringState passwordState = this.createStringState( this.createKey( "password" ), "" );
	private final PlainStringValue passwordLabelValue = this.createStringValue( this.createKey( "passwordLabel" ) );
	private final PlainStringValue titleLabelValue = this.createStringValue( this.createKey( "titleLabel" ) );
	private final StringState titleState = this.createStringState( this.createKey( "title" ), "Alice Video" );
	private final BooleanState isPrivateState = this.createBooleanState( this.createKey( "isPrivate" ), false );
	private final PlainStringValue categoryValue = this.createStringValue( this.createKey( "category" ) );
	private final ListSelectionState<String> videoCategoryState;
	private final PlainStringValue descriptionValue = this.createStringValue( this.createKey( "descriptionValue" ) );
	private final StringState descriptionState = this.createStringState( this.createKey( "description" ), "" );
	private final PlainStringValue tagLabel = this.createStringValue( this.createKey( "tagLabel" ) );
	private final StringState tagState = this.createStringState( this.createKey( "tag" ), "Alice3" );

	private final ActionOperation loginOperation = this.createActionOperation( this.createKey( "login" ), new Action() {
		public org.lgna.croquet.edits.Edit perform( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.AbstractComposite.InternalActionOperation source ) throws org.lgna.croquet.CancelException {
			try {
				uploader.logIn( idState.getValue(), passwordState.getValue() );
				isLoggedIn = true;
			} catch( AuthenticationException e ) {
				e.printStackTrace();
			}
			return null;
		}
	} );

	private final ActionOperation saveToFileOperation = this.createActionOperation( this.createKey( "save" ), new Action() {
		public org.lgna.croquet.edits.Edit perform( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.AbstractComposite.InternalActionOperation source ) throws org.lgna.croquet.CancelException {

			File file = FileDialogUtilities.showSaveFileDialog( java.util.UUID.fromString( "ba9423c8-2b6a-4d6f-a208-013136d1a680" ),
					UploadComposite.this.getView().getAwtComponent(), "Save Video", owner.getFile(), titleState.getValue(), FileUtilities.createFilenameFilter( ".webm" ), ".webm" );
			if( file != null ) {
				owner.getFile().renameTo( file );
			}
			return null;
		}
	} );

	private final ActionOperation uploadOperation = this.createActionOperation( this.createKey( "upload" ), new Action() {
		public org.lgna.croquet.edits.Edit perform( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.AbstractComposite.InternalActionOperation src ) throws org.lgna.croquet.CancelException {
			VideoEntry entry = new VideoEntry();

			MediaFileSource source = new MediaFileSource( owner.getFile(), "video/quicktime" );
			entry.setMediaSource( source );
			YouTubeMediaGroup mediaGroup = entry.getOrCreateMediaGroup();

			MediaTitle title = new MediaTitle();
			title.setPlainTextContent( titleState.getValue().trim() );
			mediaGroup.setTitle( title );//title

			MediaDescription mediaDescription = new MediaDescription();
			mediaDescription.setPlainTextContent( descriptionState.getValue().trim() );
			mediaGroup.setDescription( mediaDescription );//description]

			MediaKeywords keywords = new MediaKeywords();
			String[] arr = tagState.getValue().split( "," );
			for( String s : arr ) {
				keywords.addKeyword( s.trim() );
			}
			mediaGroup.setKeywords( keywords );//tags

			String category = videoCategoryState.getValue().toString().split( "\\s" )[ 0 ].trim();
			mediaGroup.addCategory( new MediaCategory( YouTubeNamespace.CATEGORY_SCHEME, category ) );//category
			mediaGroup.setPrivate( isPrivateState.getValue() );//isPrivate
			try {
				uploader.uploadVideo( entry );
			} catch( IOException e ) {
				e.printStackTrace();
			}
			return null;
		}
	} );
	private Status errorNotLoggedIn = createErrorStatus( this.createKey( "errorNotLoggedIn" ) );
	private boolean isLoggedIn = false;
	private Status noTittle = createErrorStatus( this.createKey( "errorNoTittle" ) );
	private Status noDescriptions = createWarningStatus( this.createKey( "warningNoDescriptions" ) );
	private Status noTags = createWarningStatus( this.createKey( "warningNoTags" ) );

	public UploadComposite( ExportToYouTubeWizardDialogComposite owner ) {
		super( java.util.UUID.fromString( "5c7ee7ee-1c0e-4a92-ac4e-bca554a0d6bc" ) );
		this.owner = owner;
		UploadComposite.initializeCategories();
		uploader.addYouTubeListener( this );
		videoCategoryState = this.createListSelectionState( this.createKey( "videoCategory" ), String.class, org.alice.ide.croquet.codecs.StringCodec.SINGLETON, 0, categoryStrings.toArray( new String[ 0 ] ) );
	}

	public StringState getIdState() {
		return this.idState;
	}

	public PlainStringValue getUsername() {
		return this.username;
	}

	public StringState getPasswordState() {
		return this.passwordState;
	}

	public PlainStringValue getPasswordLabelValue() {
		return this.passwordLabelValue;
	}

	public ActionOperation getLoginOperation() {
		return this.loginOperation;
	}

	public ActionOperation getSaveToFileOperation() {
		return this.saveToFileOperation;
	}

	public PlainStringValue getTitleLabelValue() {
		return this.titleLabelValue;
	}

	public StringState getTitleState() {
		return this.titleState;
	}

	public BooleanState getIsPrivateState() {
		return this.isPrivateState;
	}

	public PlainStringValue getCategoryValue() {
		return this.categoryValue;
	}

	public ListSelectionState<String> getVideoCategoryState() {
		return this.videoCategoryState;
	}

	public PlainStringValue getDescriptionValue() {
		return this.descriptionValue;
	}

	public StringState getDescriptionState() {
		return this.descriptionState;
	}

	public PlainStringValue getTagLabel() {
		return this.tagLabel;
	}

	public StringState getTagState() {
		return this.tagState;
	}

	public ActionOperation getUploadOperation() {
		return this.uploadOperation;
	}

	@Override
	protected UploadView createView() {
		return new UploadView( this );
	}

	public Project getProject() {
		return owner.getProject();
	}

	public File getFile() {
		return owner.getFile();
	}

	@Override
	public void handlePreActivation() {
		super.handlePreActivation();
		getView().setMovie( owner.getFile() );
	}

	@Override
	public Status getPageStatus( org.lgna.croquet.history.CompletionStep<?> step ) {
		uploadOperation.setEnabled( false );
		Status rv = IS_GOOD_TO_GO_STATUS;
		if( !isLoggedIn ) {
			setEnabled( false );
			return errorNotLoggedIn;
		} else {
			setEnabled( true );
			if( titleState.getValue().length() == 0 ) {
				return noTittle;
			} else if( descriptionState.getValue().length() == 0 ) {
				rv = noDescriptions;
			} else if( tagState.getValue().length() == 0 ) {
				rv = noTags;
			}
		}
		if( !( rv instanceof ErrorStatus ) ) {
			uploadOperation.setEnabled( true );
		}
		return rv;
	}

	private void setEnabled( boolean isEnabled ) {
		titleState.setEnabled( isEnabled );
		descriptionState.setEnabled( isEnabled );
		tagState.setEnabled( isEnabled );
		videoCategoryState.setEnabled( isEnabled );
		isPrivateState.setEnabled( isEnabled );
	}

	public void youTubeEventTriggered( YouTubeEvent event ) {
		System.out.println( "event triggered: " + event.getType() );
		if( event.getType() == EventType.UPLOAD_FAILED ) {
			System.out.println( "why failed: " );
			System.out.println( event.getMoreInfo() );
		}
	}

	/**
	 * borrowed from dave:
	 * YouTubeMeddiaGroupEditorPanel
	 */
	private static final String CATEGORY_URL = "http://gdata.youtube.com/schemas/2007/categories.cat";
	private static final String TERM_STRING = "term='";
	private static final String DEPRECATED_STRING = "deprecated";
	private static final String LABEL_STRING = "label='";
	private static final String TERM_PATTERN = "term='[^']*'";
	private static final String LABEL_PATTERN = "label='[^']*'";
	private static final String[] DEFAULT_TAGS = { "alice", "alice3" };
	private static final String DEFAULT_CATEGORY = "tech";
	private static List<String> categoryStrings;
	private static List<String> termStrings;

	private static boolean initializeCategories() {
		try {
			URL categoryURL = new URL( CATEGORY_URL );
			InputStream is = categoryURL.openStream();
			StringBuilder sb = new StringBuilder();
			int readValue;
			while( ( readValue = is.read() ) != -1 ) {
				char charVal = (char)readValue;
				sb.append( charVal );
			}
			String categoryData = sb.toString();
			List<String> labels = new LinkedList<String>();
			List<String> terms = new LinkedList<String>();
			int TERM_LENGTH = TERM_STRING.length();
			int LABEL_LENGTH = LABEL_STRING.length();
			int DEPRECATED_LENGTH = DEPRECATED_STRING.length();
			int categoryLength = categoryData.length();
			String searchTerm = TERM_STRING;
			int searchLength = TERM_LENGTH;
			for( int i = 0; i < ( categoryLength - DEPRECATED_LENGTH ); ) {
				if( categoryData.subSequence( i, i + searchLength ).equals( searchTerm ) ) {
					int endIndex = categoryData.indexOf( "'", i + searchLength );
					String foundString = categoryData.substring( i + searchLength, endIndex );
					foundString = foundString.replace( "&amp;", "&" );
					if( searchTerm == TERM_STRING ) {
						searchTerm = LABEL_STRING;
						searchLength = LABEL_LENGTH;
						terms.add( foundString );
					} else {
						searchTerm = TERM_STRING;
						searchLength = TERM_LENGTH;
						labels.add( foundString );
					}
					i = endIndex;
				} else if( categoryData.subSequence( i, i + DEPRECATED_LENGTH ).equals( DEPRECATED_STRING ) ) {
					if( terms.size() == labels.size() ) {
						terms.remove( terms.size() - 1 );
						labels.remove( labels.size() - 1 );
					} else {
						System.err.println( "CRAZY!" );
					}
					i += DEPRECATED_LENGTH;
				} else {
					i++;
				}
			}
			categoryStrings = labels;
			termStrings = terms;
			return true;
		} catch( IOException e ) {
			categoryStrings = null;
			termStrings = null;
		}
		return false;
	}
}

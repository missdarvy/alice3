/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package org.alice.stageide.modelresource;

/**
 * @author Dennis Cosgrove
 */
public abstract class ResourceNodeTreeSelectionState extends org.lgna.croquet.CustomTreeSelectionState<ResourceNode> {
	private static final javax.swing.Icon EMPTY_ICON = new edu.cmu.cs.dennisc.javax.swing.icons.EmptyIcon( 0, org.alice.ide.Theme.DEFAULT_SMALL_ICON_SIZE.height );

	private final ResourceNode root;

	public ResourceNodeTreeSelectionState( java.util.UUID migrationId, ResourceNode root ) {
		super( org.lgna.croquet.Application.DOCUMENT_UI_GROUP, migrationId, null, ResourceNodeCodec.SINGLETON );
		this.root = root;
		//todo
		this.setSwingValue( root );
	}

	@Override
	protected void setSwingValue( org.alice.stageide.modelresource.ResourceNode nextValue ) {
		super.setSwingValue( nextValue );
		if( nextValue.getResourceKey().isLeaf() ) {
			org.lgna.croquet.Model model = nextValue.getLeftButtonClickModel();
			if( model != null ) {
				edu.cmu.cs.dennisc.java.util.logging.Logger.outln( model );
				model.fire( org.lgna.croquet.triggers.NullTrigger.createUserInstance() );
			}
		}
	}

	@Override
	protected javax.swing.Icon getIconForNode( ResourceNode node ) {
		org.lgna.croquet.icon.IconFactory iconFactory = node.getResourceKey().getIconFactory();
		return iconFactory != null ? iconFactory.getIcon( org.alice.ide.Theme.DEFAULT_SMALL_ICON_SIZE ) : EMPTY_ICON;
	}

	@Override
	protected String getTextForNode( ResourceNode node ) {
		return node.getResourceKey().getDisplayText();
	}

	@Override
	protected int getChildCount( ResourceNode parent ) {
		if( this.isLeaf( parent ) ) {
			return 0;
		} else {
			return parent.getNodeChildren().size();
		}
	}

	@Override
	protected ResourceNode getChild( ResourceNode parent, int index ) {
		return parent.getNodeChildren().get( index );
	}

	@Override
	protected int getIndexOfChild( ResourceNode parent, ResourceNode child ) {
		return parent.getNodeChildren().indexOf( child );
	}

	@Override
	public ResourceNode getParent( ResourceNode node ) {
		return node.getParent();
	}

	@Override
	protected ResourceNode getRoot() {
		return this.root;
	}

	@Override
	public boolean isLeaf( ResourceNode node ) {
		assert node != null : this;
		assert node.getResourceKey() != null : node;
		return node.getResourceKey().isLeaf();
	}
}

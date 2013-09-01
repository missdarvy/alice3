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
package org.alice.stageide.gallerybrowser.uri.merge.views;

/**
 * @author Dennis Cosgrove
 */
public class MemberPopupCoreView extends org.lgna.croquet.components.BorderPanel {
	public MemberPopupCoreView( org.alice.stageide.gallerybrowser.uri.merge.MemberPopupCoreComposite composite ) {
		super( composite );
		org.alice.ide.Theme theme = org.alice.ide.theme.ThemeUtilities.getActiveTheme();
		java.awt.Color color;
		org.lgna.project.ast.Declaration member = composite.getMember();

		org.lgna.croquet.components.MigPanel panel = new org.lgna.croquet.components.MigPanel();
		if( member instanceof org.lgna.project.ast.UserMethod ) {
			org.lgna.project.ast.UserMethod method = (org.lgna.project.ast.UserMethod)member;
			panel.addComponent( new org.alice.ide.codeeditor.MethodHeaderPane( org.alice.ide.x.PreviewAstI18nFactory.getInstance(), method, true ), "wrap" );
			panel.addComponent( org.alice.ide.x.PreviewAstI18nFactory.getInstance().createComponent( method.getBodyProperty().getValue() ), "wrap" );
			if( method.isProcedure() ) {
				color = theme.getProcedureColor();
			} else {
				color = theme.getFunctionColor();
			}
		} else if( member instanceof org.lgna.project.ast.UserField ) {
			org.lgna.project.ast.UserField field = (org.lgna.project.ast.UserField)member;
			panel.addComponent( new org.alice.ide.common.FieldDeclarationPane( org.alice.ide.x.PreviewAstI18nFactory.getInstance(), field ), "wrap" );
			color = theme.getFieldColor();
		} else {
			//todo
			color = theme.getConstructorColor();
		}
		panel.setBackgroundColor( color );
		org.lgna.croquet.components.AbstractLabel label = composite.getDescription().createLabel();
		label.setIcon( composite.getIcon() );
		this.addPageStartComponent( label );
		this.addCenterComponent( panel );

		label.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 0, 4, 0 ) );
		this.setBorder( javax.swing.BorderFactory.createMatteBorder( 4, 4, 4, 4, java.awt.Color.WHITE ) );
		this.setMinimumPreferredWidth( 200 );
	}
}

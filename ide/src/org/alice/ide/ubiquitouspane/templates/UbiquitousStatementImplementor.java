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
package org.alice.ide.ubiquitouspane.templates;

/**
 * @author Dennis Cosgrove
 */
public class UbiquitousStatementImplementor { //todo: needs a better name
	private org.lgna.project.ast.Statement incompleteStatement;
	private org.lgna.croquet.components.Component< ? > incompleteStatementPane;
	private String labelText;
	private javax.swing.JToolTip toolTip;

	public UbiquitousStatementImplementor( org.lgna.project.ast.Statement incompleteStatement ) {
		this.incompleteStatement = incompleteStatement;
	}
	public org.lgna.project.ast.Statement getIncompleteStatement() {
		return this.incompleteStatement;
	}
	public org.lgna.croquet.components.Component< ? > getIncompleteStatementPane() {
		if( this.incompleteStatementPane != null ) {
			//pass
		} else {
			org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
			this.incompleteStatementPane = org.alice.ide.x.TemplateAstI18nFactory.getInstance().createStatementPane( incompleteStatement );
		}
		return this.incompleteStatementPane;
	}
	public String getLabelText() {
		if( this.labelText != null ) {
			//pass
		} else {
			Class<?> cls = incompleteStatement.getClass();
			this.labelText = edu.cmu.cs.dennisc.java.util.ResourceBundleUtilities.getStringFromSimpleNames( cls, "org.alice.ide.ubiquitouspane.Templates", org.alice.ide.croquet.models.ui.formatter.FormatterSelectionState.getInstance().getSelectedItem().getLocale() );
		}
		return this.labelText;
	}
	public javax.swing.JToolTip getToolTip() {
		if( this.toolTip != null ) {
			//pass
		} else {
			this.toolTip = new edu.cmu.cs.dennisc.javax.swing.tooltips.JToolTip( this.getIncompleteStatementPane().getAwtComponent() );
		}
		return this.toolTip;
	}
	public java.awt.Dimension adjustMinimumSize( java.awt.Dimension rv ) {
		rv.width = Math.min( rv.width, 24 );
		return rv;
	}
	
}

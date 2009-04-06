/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package org.alice.ide.operations.ast;

/**
 * @author Dennis Cosgrove
 */
public abstract class RenameNodeOperation extends org.alice.ide.operations.AbstractActionOperation {
	private edu.cmu.cs.dennisc.property.StringProperty nameProperty;
	private String prevValue;
	private String nextValue;
	public RenameNodeOperation( edu.cmu.cs.dennisc.property.StringProperty nameProperty ) {
		this.nameProperty = nameProperty;
		this.putValue( javax.swing.Action.NAME, "rename..." );
	}
	public void perform( zoot.ActionContext actionContext ) {
		this.nextValue = javax.swing.JOptionPane.showInputDialog( "name" );
		if( nextValue != null && nextValue.length() > 0 ) {
			this.prevValue = nameProperty.getValue();
			this.redo();
			actionContext.commit();
		} else {
			actionContext.cancel();
		}
	}

	public void redo() {
		this.nameProperty.setValue( this.nextValue );
	}
	public void undo() {
		this.nameProperty.setValue( this.prevValue );
	}
}

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
package edu.cmu.cs.dennisc.zoot;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractEdit implements Edit {
	public abstract void doOrRedo();
	public abstract void undo();
	public boolean canDoAndRedo() {
		return true;
	}
	public boolean canUndo() {
		return true;
	}
	
	protected StringBuffer updatePresentation( StringBuffer rv, java.util.Locale locale ) {
		rv.append( this.getClass().getSimpleName() );
		return rv;
	}
	public final String getPresentation( java.util.Locale locale ) {
		StringBuffer sb = new StringBuffer();
		this.updatePresentation( sb, locale );
		return sb.toString();
	}
}

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

package edu.cmu.cs.dennisc.scenegraph.graphics;

public abstract class Title extends ShapeEnclosedText {
	@Override
	protected edu.cmu.cs.dennisc.color.Color4f getDefaultTextColor() {
		return edu.cmu.cs.dennisc.color.Color4f.WHITE;
	}
	@Override
	protected edu.cmu.cs.dennisc.color.Color4f getDefaultFillColor() {
		return edu.cmu.cs.dennisc.color.Color4f.BLACK;
	}
	@Override
	protected edu.cmu.cs.dennisc.color.Color4f getDefaultOutlineColor() {
		return edu.cmu.cs.dennisc.color.Color4f.createNaN();
	}
	@Override
	protected java.awt.Font getDefaultFont() {
		return new java.awt.Font( null, java.awt.Font.PLAIN, 24 );
	}
}

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
package edu.cmu.cs.dennisc.alice.ast;

/**
 * @author Dennis Cosgrove
 */
public class TypeLiteral extends AbstractValueLiteral {
	public DeclarationProperty<AbstractType> value = new DeclarationProperty<AbstractType>( this );
	public TypeLiteral() {
	}
	public TypeLiteral( AbstractType value ) {
		this.value.setValue( value );
	}
	public TypeLiteral( Class< ? > cls ) {
		this( TypeDeclaredInJava.get( cls ) );
	}
	@Override
	public AbstractType getType() {
		//todo
		return TypeDeclaredInJava.get( value.getValue().getClass() );
	}
	@Override
	protected StringBuffer appendRepr( StringBuffer rv, java.util.Locale locale ) {
		rv.append( this.value.getValue() );
		return rv;
	}
}

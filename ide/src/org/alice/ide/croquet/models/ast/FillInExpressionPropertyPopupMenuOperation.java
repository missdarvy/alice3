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
package org.alice.ide.croquet.models.ast;

/**
 * @author Dennis Cosgrove
 */
public abstract class FillInExpressionPropertyPopupMenuOperation extends FillInSingleExpressionPopupMenuOperation {
//	private static java.util.Map< edu.cmu.cs.dennisc.alice.ast.ExpressionProperty, FillInExpressionPropertyPopupMenuOperation > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
//	public static synchronized FillInExpressionPropertyPopupMenuOperation getInstance( edu.cmu.cs.dennisc.croquet.Group operationGroup, edu.cmu.cs.dennisc.alice.ast.ExpressionProperty expressionProperty, edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> desiredType ) {
//		FillInExpressionPropertyPopupMenuOperation rv = map.get( expressionProperty );
//		if( rv != null ) {
//			assert rv.operationGroup == operationGroup;
//			assert rv.desiredType == desiredType;
//			//pass
//		} else {
//			rv = new FillInExpressionPropertyPopupMenuOperation( operationGroup, expressionProperty, desiredType );
//			map.put( expressionProperty, rv );
//		}
//		return rv;
//	}
//	private final edu.cmu.cs.dennisc.croquet.Group operationGroup;
//	private final edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> desiredType;
//	private final edu.cmu.cs.dennisc.alice.ast.ExpressionProperty expressionProperty;
//	private FillInExpressionPropertyPopupMenuOperation( edu.cmu.cs.dennisc.croquet.Group operationGroup, edu.cmu.cs.dennisc.alice.ast.ExpressionProperty expressionProperty, edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> desiredType ) {
//		super( java.util.UUID.fromString( "c89cd38a-693a-49c0-a4fd-74df439f54fd" ) );
//		assert expressionProperty != null;
//		this.operationGroup = operationGroup;
//		this.expressionProperty = expressionProperty;
//		this.desiredType = desiredType;
//	}
//	@Override
//	protected edu.cmu.cs.dennisc.alice.ast.Expression getPreviousExpression() {
//		return this.expressionProperty.getValue();
//	}
//	@Override
//	protected String getTitle() {
//		return this.expressionProperty.getName();
//	}
//	@Override
//	protected edu.cmu.cs.dennisc.alice.ast.AbstractType< ?, ?, ? > getDesiredValueType() {
//		edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type;
//		if( this.desiredType != null ) {
//			return this.desiredType;
//		} else {
//			return this.expressionProperty.getExpressionType();
//		}
//	}
	public FillInExpressionPropertyPopupMenuOperation( java.util.UUID id ) {
		super( id );
	}

	protected abstract edu.cmu.cs.dennisc.alice.ast.ExpressionProperty getExpressionProperty();
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.Expression getPreviousExpression() {
		edu.cmu.cs.dennisc.alice.ast.ExpressionProperty expressionProperty = this.getExpressionProperty();
		return expressionProperty.getValue();
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.AbstractType< ?, ?, ? > getDesiredValueType() {
		edu.cmu.cs.dennisc.alice.ast.ExpressionProperty expressionProperty = this.getExpressionProperty();
		return expressionProperty.getExpressionType();
	}
	
}

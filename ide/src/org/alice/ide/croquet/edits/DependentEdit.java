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

package org.alice.ide.croquet.edits;

/**
 * @author Dennis Cosgrove
 */
public final class DependentEdit<M extends edu.cmu.cs.dennisc.croquet.Operation<?>> extends edu.cmu.cs.dennisc.croquet.OperationEdit< M > {
	@Override
	public edu.cmu.cs.dennisc.croquet.Edit.Memento< M > createMemento() {
		throw new RuntimeException( "todo" );
	}
	private org.alice.ide.croquet.models.ResponsibleModel getResponsibleModel() {
		edu.cmu.cs.dennisc.croquet.ModelContext< ? > context = this.getContext();
		if( context != null ) {
			edu.cmu.cs.dennisc.croquet.Model model = context.getModel();
			if( model instanceof org.alice.ide.croquet.models.ResponsibleModel ) {
				return (org.alice.ide.croquet.models.ResponsibleModel)model;
			} else {
				throw new RuntimeException();
			}
		} else {
			throw new NullPointerException();
		}
	}
	@Override
	public void retarget( edu.cmu.cs.dennisc.croquet.Retargeter retargeter ) {
		super.retarget( retargeter );
		this.getResponsibleModel().retarget( retargeter );
	}
	
	@Override
	protected void doOrRedoInternal( boolean isDo ) {
		this.getResponsibleModel().doOrRedoInternal( isDo );
	}

	@Override
	protected void undoInternal() {
		this.getResponsibleModel().undoInternal();
	}
	
	@Override
	protected StringBuilder updatePresentation( StringBuilder rv, java.util.Locale locale ) {
		return this.getResponsibleModel().updatePresentation( rv, locale );
	}
	@Override
	public edu.cmu.cs.dennisc.croquet.ReplacementAcceptability getReplacementAcceptability( edu.cmu.cs.dennisc.croquet.Edit< ? > replacementCandidate, edu.cmu.cs.dennisc.croquet.UserInformation userInformation ) {
		return this.getResponsibleModel().getReplacementAcceptability( replacementCandidate, userInformation );
	}
	@Override
	public void addKeyValuePairs( edu.cmu.cs.dennisc.croquet.Retargeter retargeter, edu.cmu.cs.dennisc.croquet.Edit< ? > edit ) {
		super.addKeyValuePairs( retargeter, edit );
		this.getResponsibleModel().addKeyValuePairs( retargeter, edit );
	}
}

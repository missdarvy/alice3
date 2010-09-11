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
package edu.cmu.cs.dennisc.croquet.tutorial;

/**
 * @author Dennis Cosgrove
 */
class IsAcceptableCommitOf extends IsChildOfAndInstanceOf< edu.cmu.cs.dennisc.croquet.CommitEvent > {
	private edu.cmu.cs.dennisc.croquet.CommitEvent originalCommitEvent;
	public IsAcceptableCommitOf( ParentContextCriterion parentContextCriterion, edu.cmu.cs.dennisc.croquet.CommitEvent originalCommitEvent ) {
		super( parentContextCriterion, edu.cmu.cs.dennisc.croquet.CommitEvent.class );
		this.originalCommitEvent = originalCommitEvent;
	}
	@Override
	protected boolean isSpecificallyWhatWereLookingFor( edu.cmu.cs.dennisc.croquet.CommitEvent commitEvent ) throws CancelException {
		boolean rv = super.isSpecificallyWhatWereLookingFor( commitEvent );
		if( rv ) {
			edu.cmu.cs.dennisc.croquet.Edit< ? > potentialReplacementEdit = commitEvent.getEdit();
			if( this.originalCommitEvent != null ) {
				edu.cmu.cs.dennisc.croquet.Edit< ? > originalEdit = this.originalCommitEvent.getEdit();
				if( originalEdit.isReplacementAcceptable( potentialReplacementEdit ) ) {
					edu.cmu.cs.dennisc.croquet.Retargeter retargeter = AutomaticTutorial.getInstance().getRetargeter();
					originalEdit.addKeyValuePairs( retargeter, potentialReplacementEdit );
					AutomaticTutorial.getInstance().retargetOriginalContext( retargeter );
				} else {
					throw new CancelException( "unacceptable: replacement edit does not pass muster." );
				}
			} else {
				if( potentialReplacementEdit != null ) {
					throw new CancelException( "unacceptable: replacement edit is null." );
				} else {
					//pass
				}
			}
		}
		return rv;
	}
}

/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.alice.ide.preferences.recursion;

import edu.cmu.cs.dennisc.pattern.Lazy;
import org.alice.ide.preferences.recursion.components.IsRecursionAllowedPreferenceView;
import org.lgna.croquet.Application;
import org.lgna.croquet.LazyOperationUnadornedDialogCoreComposite;
import org.lgna.croquet.Operation;
import org.lgna.croquet.PlainStringValue;
import org.lgna.croquet.imp.launch.LazySimpleLaunchOperationFactory;

import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public final class IsRecursionAllowedPreferenceDialogComposite extends LazyOperationUnadornedDialogCoreComposite<IsRecursionAllowedPreferenceView> {
  private IsRecursionAllowedPreferenceDialogComposite(int index) {
    super(UUID.fromString("877a3f9a-40c0-4100-90a3-6fb736ed5305"));
    this.depth = index;
    this.next = LazySimpleLaunchOperationFactory.createInstance(IsRecursionAllowedPreferenceDialogComposite.class, new Lazy<IsRecursionAllowedPreferenceDialogComposite>() {
      @Override
      protected IsRecursionAllowedPreferenceDialogComposite create() {
        return new IsRecursionAllowedPreferenceDialogComposite(depth + 1);
      }
    }, Application.APPLICATION_UI_GROUP).getLaunchOperation();
  }

  public IsRecursionAllowedPreferenceDialogComposite() {
    this(0);
  }

  @Override
  protected IsRecursionAllowedPreferenceView createView() {
    return new IsRecursionAllowedPreferenceView(this);
  }

  public PlainStringValue getDescriptionText() {
    return this.descriptionText;
  }

  public PlainStringValue getRecursiveButtonText() {
    return this.recursiveButtonText;
  }

  public Operation getNext() {
    return this.next;
  }

  private final PlainStringValue descriptionText = this.createStringValue("descriptionText");
  private final PlainStringValue recursiveButtonText = this.createStringValue("recursiveButtonText");
  private final int depth;
  private final Operation next;
}

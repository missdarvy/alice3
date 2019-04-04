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
package org.alice.ide.croquet.models.cascade;

import edu.cmu.cs.dennisc.java.util.InitializingIfAbsentMap;
import edu.cmu.cs.dennisc.java.util.Maps;
import org.alice.ide.IDE;
import org.alice.ide.common.TypeIcon;
import org.lgna.croquet.CascadeBlankChild;
import org.lgna.croquet.imp.cascade.BlankNode;
import org.lgna.croquet.imp.cascade.ItemNode;
import org.lgna.project.annotations.ValueDetails;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.JavaType;

import javax.swing.Icon;
import java.util.List;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public final class TypeExpressionCascadeMenu extends ExpressionCascadeMenu<Expression> {
  private static InitializingIfAbsentMap<AbstractType, TypeExpressionCascadeMenu> map = Maps.newInitializingIfAbsentHashMap();

  public static TypeExpressionCascadeMenu getInstance(AbstractType type) {
    return map.getInitializingIfAbsent(type, new InitializingIfAbsentMap.Initializer<AbstractType, TypeExpressionCascadeMenu>() {
      @Override
      public TypeExpressionCascadeMenu initialize(AbstractType key) {
        return new TypeExpressionCascadeMenu(key, null);
      }
    });
  }

  public static TypeExpressionCascadeMenu getInstance(Class<?> cls) {
    return getInstance(JavaType.getInstance(cls));
  }

  private final AbstractType<?, ?, ?> valueType;
  private final ValueDetails<?> details;

  private TypeExpressionCascadeMenu(AbstractType<?, ?, ?> valueType, ValueDetails<?> details) {
    super(UUID.fromString("abafdc1c-7e12-4db4-94b2-17120c6a7110"));
    this.valueType = valueType;
    this.details = details;
  }

  @Override
  protected void updateBlankChildren(List<CascadeBlankChild> blankChildren, BlankNode<Expression> blankNode) {
    IDE ide = IDE.getActiveInstance();
    if (ide != null) {
      ide.getExpressionCascadeManager().appendItems(blankChildren, blankNode, this.valueType, this.details);
    }
  }

  @Override
  public Icon getMenuItemIcon(ItemNode<? super Expression, Expression> node) {
    return TypeIcon.getInstance(this.valueType);
  }

}

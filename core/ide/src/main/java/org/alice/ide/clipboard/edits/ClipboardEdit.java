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
package org.alice.ide.clipboard.edits;

import edu.cmu.cs.dennisc.codec.BinaryDecoder;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;
import org.alice.ide.IDE;
import org.alice.ide.ast.draganddrop.BlockStatementIndexPair;
import org.alice.ide.clipboard.Clipboard;
import org.alice.ide.issue.croquet.AnomalousSituationComposite;
import org.lgna.croquet.edits.AbstractEdit;
import org.lgna.croquet.history.UserActivity;
import org.lgna.project.ProgramTypeUtilities;
import org.lgna.project.Project;
import org.lgna.project.ast.Node;
import org.lgna.project.ast.NodeUtilities;
import org.lgna.project.ast.Statement;

import javax.swing.SwingUtilities;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public abstract class ClipboardEdit extends AbstractEdit {
  private final Statement statement;
  private final BlockStatementIndexPair blockStatementIndexPair;

  public ClipboardEdit(UserActivity userActivity, Statement statement, BlockStatementIndexPair blockStatementIndexPair) {
    super(userActivity);
    this.statement = statement;
    this.blockStatementIndexPair = blockStatementIndexPair;
  }

  public ClipboardEdit(BinaryDecoder binaryDecoder, Object step) {
    super(binaryDecoder, step);
    IDE ide = IDE.getActiveInstance();
    Project project = ide.getProject();
    UUID statementId = binaryDecoder.decodeId();
    this.statement = ProgramTypeUtilities.lookupNode(project, statementId);
    this.blockStatementIndexPair = binaryDecoder.decodeBinaryEncodableAndDecodable();
  }

  public Statement getStatement() {
    return this.statement;
  }

  @Override
  public void encode(BinaryEncoder binaryEncoder) {
    super.encode(binaryEncoder);
    binaryEncoder.encode(this.statement.getId());
    binaryEncoder.encode(this.blockStatementIndexPair);
  }

  protected void pushAndRemove() {
    Clipboard.SINGLETON.push(this.statement);
    this.blockStatementIndexPair.getBlockStatement().statements.remove(this.blockStatementIndexPair.getIndex());
  }

  protected void popAndAdd() {
    Node node = Clipboard.SINGLETON.pop();
    if (node == this.statement) {
      this.blockStatementIndexPair.getBlockStatement().statements.add(this.blockStatementIndexPair.getIndex(), this.statement);
    } else {
      StringBuilder sb = new StringBuilder();
      try {
        NodeUtilities.safeAppendRepr(sb, statement);
      } catch (Throwable t) {
        sb.append(statement);
      }
      sb.append(";");
      sb.append(statement.getId());
      final AnomalousSituationComposite composite = AnomalousSituationComposite.createInstance("Oh no!  The clipboard is in a bad state.  You may want to save your project and restart Alice.", sb.toString());
      SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
          composite.getLaunchOperation().fire();
        }
      });
    }
  }
}

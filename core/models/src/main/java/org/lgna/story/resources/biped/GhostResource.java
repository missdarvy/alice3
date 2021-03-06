/*
 * Alice 3 End User License Agreement
 *
 * Copyright (c) 2006-2015, Carnegie Mellon University. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may "Alice" appear in their name, without prior written permission of Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must display the following acknowledgement: "This product includes software developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is contributed by Electronic Arts Inc. and may be used for personal, non-commercial, and academic use only. Redistributions of any program source code that utilizes The Sims 2 Assets must also retain the copyright notice, list of conditions and the disclaimer contained in The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package org.lgna.story.resources.biped;

import org.lgna.project.annotations.FieldTemplate;
import org.lgna.project.annotations.Visibility;
import org.lgna.story.JointedModelPose;
import org.lgna.story.SBiped;
import org.lgna.story.implementation.BipedImp;
import org.lgna.story.implementation.JointIdTransformationPair;
import org.lgna.story.Orientation;
import org.lgna.story.Position;
import org.lgna.story.implementation.JointedModelImp;
import org.lgna.story.resources.BipedResource;
import org.lgna.story.resources.ImplementationAndVisualType;
import org.lgna.story.resources.JointId;
import org.lgna.story.resources.JointedModelResource;

public enum GhostResource implements BipedResource {
  SHEET, MITTS, DEFAULT;

  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId LOWER_LIP = new JointId(MOUTH, GhostResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId LEFT_INDEX_FINGER_TIP = new JointId(LEFT_INDEX_FINGER_KNUCKLE, GhostResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId LEFT_THUMB_TIP = new JointId(LEFT_THUMB_KNUCKLE, GhostResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId RIGHT_INDEX_FINGER_TIP = new JointId(RIGHT_INDEX_FINGER_KNUCKLE, GhostResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId RIGHT_THUMB_TIP = new JointId(RIGHT_THUMB_KNUCKLE, GhostResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId LEFT_TOES = new JointId(LEFT_FOOT, GhostResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId RIGHT_TOES = new JointId(RIGHT_FOOT, GhostResource.class);

  public static final JointedModelPose CREEPING_POSE = new JointedModelPose(new JointIdTransformationPair(LEFT_HAND, new Orientation(-0.1965038708122167, -0.4027165454209268, 0.42045403897141076, 0.7889385362071483), new Position(-7.77156120546332E-18, -2.8421708795129297E-16, -0.047627706080675125)), new JointIdTransformationPair(RIGHT_WRIST, new Orientation(-0.011855369576217484, 0.22368657232118294, -0.03140317621559547, 0.9740829575036121), new Position(-8.881783998477905E-18, -2.8421708795129297E-16, -0.20072419941425323)), new JointIdTransformationPair(LEFT_EYE, new Orientation(0.10481637607089465, 0.08710637279234093, 0.011390172983206766, 0.990603993069753), new Position(-0.05225023254752159, 0.1934623271226883, -0.12080084532499313)),
                                                                            new JointIdTransformationPair(LEFT_SHOULDER, new Orientation(0.03654206377392616, -0.2801501919794684, 0.19875964269652144, 0.9384429401642704), new Position(1.776356799695581E-17, -4.2632563192693945E-16, -0.11649160087108612)), new JointIdTransformationPair(LEFT_INDEX_FINGER, new Orientation(-0.05495302168797432, -0.028183740694562906, -0.26724523736863154, 0.9616474537332309), new Position(1.4210854397564648E-16, 4.2632563192693945E-16, -0.04404425993561745)), new JointIdTransformationPair(RIGHT_HAND, new Orientation(0.08063515911842621, 0.8218366539932599, -0.42218616709460666, 0.37395364095175443), new Position(3.6637357959745594E-17, 2.8421708795129297E-16, -0.047627970576286316)),
                                                                            new JointIdTransformationPair(RIGHT_SHOULDER, new Orientation(-0.020650727649637775, 0.264241455411559, -0.1780936560857719, 0.9476458464803844), new Position(-7.105427198782324E-17, -8.526512638538789E-16, -0.11649136245250702)), new JointIdTransformationPair(RIGHT_THUMB, new Orientation(-0.29254408540411697, -0.2523651147593063, 0.05716005636028463, 0.9205772835044758), new Position(-1.4210854397564648E-16, -0.004005323629826307, -0.031017616391181946)), new JointIdTransformationPair(RIGHT_THUMB_KNUCKLE, new Orientation(-0.034260283026216524, -0.1779090052515769, 0.07041435929135738, 0.980926315715378), new Position(-2.1316281596346973E-16, 1.4210854397564648E-16, -0.03466140478849411)),
                                                                            new JointIdTransformationPair(RIGHT_INDEX_FINGER_KNUCKLE, new Orientation(-0.09137623588561432, -0.014643960696676981, 0.26111083878483976, 0.9608626685429964), new Position(-2.1316281596346973E-16, 0.0, -0.035351965576410294)), new JointIdTransformationPair(LEFT_WRIST, new Orientation(0.028252136846934083, -0.192889534308765, -0.0023046452731996106, 0.9808109567739823), new Position(-3.552713599391162E-17, 1.4210854397564648E-16, -0.2007245570421219)), new JointIdTransformationPair(RIGHT_INDEX_FINGER, new Orientation(-0.23379885355000016, -0.6887423589526235, 0.13664855688307448, 0.6725319553484833), new Position(0.043887585401535034, -2.496026863809675E-4, 0.0037056312430649996)),
                                                                            new JointIdTransformationPair(RIGHT_ELBOW, new Orientation(0.17717905606696968, 0.048490700291939415, -0.19409406943419674, 0.9636304926095314), new Position(1.4210854397564648E-16, 8.526512638538789E-16, -0.22879430651664734)), new JointIdTransformationPair(RIGHT_EYE, new Orientation(0.08602272395712636, -0.0766646856770279, -0.0014694420593340408, 0.993338037967475), new Position(0.052250199019908905, 0.19345971941947937, -0.12080083042383194)), new JointIdTransformationPair(LEFT_INDEX_FINGER_KNUCKLE, new Orientation(-0.09168236801771239, 0.012572969000758183, -0.2392832028693546, 0.9665297784702518), new Position(1.4210854397564648E-16, -4.2632563192693945E-16, -0.03535260632634163)),
                                                                            new JointIdTransformationPair(LEFT_ELBOW, new Orientation(0.17549851484393822, -0.05426212757653594, 0.22551945285463307, 0.9567637478409112), new Position(-1.6875389762544143E-16, 2.8421708795129297E-16, -0.22879402339458466)), new JointIdTransformationPair(LEFT_CLAVICLE, new Orientation(-0.18537713347433873, 0.33730387168068726, -0.7929966075967849, 0.4722476012369427), new Position(-0.05466973036527634, -0.052855007350444794, 0.020314112305641174)), new JointIdTransformationPair(RIGHT_CLAVICLE, new Orientation(-0.1784958880060792, -0.3257465992268421, 0.7978140376723765, 0.4748906530454859), new Position(0.05466970056295395, -0.052855055779218674, 0.02031453512609005)),
                                                                            new JointIdTransformationPair(LEFT_THUMB, new Orientation(-0.15493361388147062, -0.505874634381738, -0.2576504623188494, 0.8085188116832245), new Position(0.030886158347129822, -0.004183998331427574, 0.002586632501333952)));

  public static final JointedModelPose FLOATING_POSE = new JointedModelPose(new JointIdTransformationPair(LEFT_HAND, new Orientation(0.024431752912424103, 0.14346747926479664, 0.010370799971493672, 0.9892990540534154), new Position(-7.77156120546332E-18, -2.8421708795129297E-16, -0.047627706080675125)), new JointIdTransformationPair(LEFT_EYE, new Orientation(0.10481637607089465, 0.08710637279234093, 0.011390172983206766, 0.990603993069753), new Position(-0.05225023254752159, 0.1934623271226883, -0.12080084532499313)), new JointIdTransformationPair(LEFT_SHOULDER, new Orientation(-0.1575832551863549, -0.24839207463981314, -0.09280473994765444, 0.9512392838735425), new Position(1.776356799695581E-17, -4.2632563192693945E-16, -0.11649160087108612)),
                                                                            new JointIdTransformationPair(LEFT_INDEX_FINGER, new Orientation(-0.05495302168797432, -0.028183740694562906, -0.26724523736863154, 0.9616474537332309), new Position(1.4210854397564648E-16, 4.2632563192693945E-16, -0.04404425993561745)), new JointIdTransformationPair(RIGHT_SHOULDER, new Orientation(-0.15758440538977952, 0.2483928216547841, 0.09280456076427736, 0.9512389157460253), new Position(-7.105427198782324E-17, -8.526512638538789E-16, -0.11649136245250702)), new JointIdTransformationPair(RIGHT_HAND, new Orientation(0.19256351372101443, 0.6183908041140143, -0.09509696590422581, 0.7559554706769771), new Position(3.6637357959745594E-17, 2.8421708795129297E-16, -0.047627970576286316)),
                                                                            new JointIdTransformationPair(RIGHT_THUMB, new Orientation(-0.29254408540411697, -0.2523651147593063, 0.05716005636028463, 0.9205772835044758), new Position(-1.4210854397564648E-16, -0.004005323629826307, -0.031017616391181946)), new JointIdTransformationPair(RIGHT_THUMB_KNUCKLE, new Orientation(-0.034260283026216524, -0.1779090052515769, 0.07041435929135738, 0.980926315715378), new Position(-2.1316281596346973E-16, 1.4210854397564648E-16, -0.03466140478849411)), new JointIdTransformationPair(RIGHT_INDEX_FINGER_KNUCKLE, new Orientation(-0.09137623588561432, -0.014643960696676981, 0.26111083878483976, 0.9608626685429964), new Position(-2.1316281596346973E-16, 0.0, -0.035351965576410294)),
                                                                            new JointIdTransformationPair(RIGHT_INDEX_FINGER, new Orientation(-0.23379885355000016, -0.6887423589526235, 0.13664855688307448, 0.6725319553484833), new Position(0.043887585401535034, -2.496026863809675E-4, 0.0037056312430649996)), new JointIdTransformationPair(RIGHT_ELBOW, new Orientation(0.18331662009798247, 0.011780768771849085, 0.004132304945126776, 0.9829746458270613), new Position(1.4210854397564648E-16, 8.526512638538789E-16, -0.22879430651664734)), new JointIdTransformationPair(RIGHT_EYE, new Orientation(0.08602272395712636, -0.0766646856770279, -0.0014694420593340408, 0.993338037967475), new Position(0.052250199019908905, 0.19345971941947937, -0.12080083042383194)),
                                                                            new JointIdTransformationPair(LEFT_INDEX_FINGER_KNUCKLE, new Orientation(-0.09168236801771239, 0.012572969000758183, -0.2392832028693546, 0.9665297784702518), new Position(1.4210854397564648E-16, -4.2632563192693945E-16, -0.03535260632634163)), new JointIdTransformationPair(LEFT_ELBOW, new Orientation(0.18331755583580167, -0.011780758124183605, -0.004132293343653571, 0.9829744714956382), new Position(-1.6875389762544143E-16, 2.8421708795129297E-16, -0.22879402339458466)), new JointIdTransformationPair(LEFT_THUMB, new Orientation(-0.15493361388147062, -0.505874634381738, -0.2576504623188494, 0.8085188116832245), new Position(0.030886158347129822, -0.004183998331427574, 0.002586632501333952)));

  public static final JointedModelPose HAUNTING_POSE = new JointedModelPose(new JointIdTransformationPair(LEFT_HAND, new Orientation(-0.08510964664752639, 0.14144094551244069, 0.026171092553025528, 0.9859340144729063), new Position(-7.77156120546332E-18, -2.8421708795129297E-16, -0.047627706080675125)), new JointIdTransformationPair(RIGHT_WRIST, new Orientation(0.031176597567351853, -0.029782044368235533, -0.10363359388791483, 0.9936805964771812), new Position(-8.881783998477905E-18, -2.8421708795129297E-16, -0.20072419941425323)), new JointIdTransformationPair(LEFT_EYE, new Orientation(0.10481637607089465, 0.08710637279234093, 0.011390172983206766, 0.990603993069753), new Position(-0.05225023254752159, 0.1934623271226883, -0.12080084532499313)),
                                                                            new JointIdTransformationPair(LEFT_SHOULDER, new Orientation(0.028281975354951736, -0.7802319868914805, -0.06352117728146088, 0.6216134140590691), new Position(1.776356799695581E-17, -4.2632563192693945E-16, -0.11649160087108612)), new JointIdTransformationPair(LEFT_INDEX_FINGER, new Orientation(-0.05495302168797432, -0.028183740694562906, -0.26724523736863154, 0.9616474537332309), new Position(1.4210854397564648E-16, 4.2632563192693945E-16, -0.04404425993561745)), new JointIdTransformationPair(RIGHT_SHOULDER, new Orientation(0.06334892865414957, 0.7923206430847514, 0.0788707367536492, 0.6016596368912194), new Position(-7.105427198782324E-17, -8.526512638538789E-16, -0.11649136245250702)),
                                                                            new JointIdTransformationPair(RIGHT_HAND, new Orientation(0.10651105285807032, 0.6388609360360621, -0.19721017458582368, 0.7359485356096722), new Position(3.6637357959745594E-17, 2.8421708795129297E-16, -0.047627970576286316)), new JointIdTransformationPair(RIGHT_THUMB, new Orientation(-0.29254408540411697, -0.2523651147593063, 0.05716005636028463, 0.9205772835044758), new Position(-1.4210854397564648E-16, -0.004005323629826307, -0.031017616391181946)), new JointIdTransformationPair(RIGHT_THUMB_KNUCKLE, new Orientation(-0.034260283026216524, -0.1779090052515769, 0.07041435929135738, 0.980926315715378), new Position(-2.1316281596346973E-16, 1.4210854397564648E-16, -0.03466140478849411)),
                                                                            new JointIdTransformationPair(RIGHT_INDEX_FINGER_KNUCKLE, new Orientation(-0.09137623588561432, -0.014643960696676981, 0.26111083878483976, 0.9608626685429964), new Position(-2.1316281596346973E-16, 0.0, -0.035351965576410294)), new JointIdTransformationPair(LEFT_WRIST, new Orientation(0.03411965236742588, 0.026350584496971766, 0.20724767166093544, 0.9773381700364081), new Position(-3.552713599391162E-17, 1.4210854397564648E-16, -0.2007245570421219)), new JointIdTransformationPair(RIGHT_INDEX_FINGER, new Orientation(-0.23379885355000016, -0.6887423589526235, 0.13664855688307448, 0.6725319553484833), new Position(0.043887585401535034, -2.496026863809675E-4, 0.0037056312430649996)),
                                                                            new JointIdTransformationPair(RIGHT_ELBOW, new Orientation(0.17462972999749327, 0.28987485109781974, 0.055915192948896665, 0.9393351474843827), new Position(1.4210854397564648E-16, 8.526512638538789E-16, -0.22879430651664734)), new JointIdTransformationPair(RIGHT_EYE, new Orientation(0.08602272395712636, -0.0766646856770279, -0.0014694420593340408, 0.993338037967475), new Position(0.052250199019908905, 0.19345971941947937, -0.12080083042383194)), new JointIdTransformationPair(LEFT_INDEX_FINGER_KNUCKLE, new Orientation(-0.09168236801771239, 0.012572969000758183, -0.2392832028693546, 0.9665297784702518), new Position(1.4210854397564648E-16, -4.2632563192693945E-16, -0.03535260632634163)),
                                                                            new JointIdTransformationPair(LEFT_ELBOW, new Orientation(0.17449345695892787, -0.29216992600102626, -0.05634213027409214, 0.9386236371279251), new Position(-1.6875389762544143E-16, 2.8421708795129297E-16, -0.22879402339458466)), new JointIdTransformationPair(LEFT_THUMB, new Orientation(-0.15493361388147062, -0.505874634381738, -0.2576504623188494, 0.8085188116832245), new Position(0.030886158347129822, -0.004183998331427574, 0.002586632501333952)));

  private final ImplementationAndVisualType resourceType;

  GhostResource() {
    this(ImplementationAndVisualType.ALICE);
  }

  GhostResource(ImplementationAndVisualType resourceType) {
    this.resourceType = resourceType;
  }

  @Override
  public JointedModelImp.JointImplementationAndVisualDataFactory<JointedModelResource> getImplementationAndVisualFactory() {
    return this.resourceType.getFactory(this);
  }

  @Override
  public BipedImp createImplementation(SBiped abstraction) {
    return new BipedImp(abstraction, this.resourceType.getFactory(this));
  }
}

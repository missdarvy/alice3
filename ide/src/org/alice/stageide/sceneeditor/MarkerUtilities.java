/*
 * Copyright (c) 2006-2011, Carnegie Mellon University. All rights reserved.
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
package org.alice.stageide.sceneeditor;

import javax.swing.Icon;

import org.alice.ide.name.validators.MarkerColorValidator;
import org.alice.stageide.sceneeditor.viewmanager.MarkerManagerPanel;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.UserField;

/**
 * @author dculyba
 *
 */
public class MarkerUtilities {
	private static final String DEFAULT_CAMERA_MARKER_NAME;
	private static final String[] COLOR_NAMES;
	private static final org.lgna.story.Color[] COLORS;
	
	static
	{
		java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle( MarkerManagerPanel.class.getPackage().getName() + ".cameraMarkers" );
		DEFAULT_CAMERA_MARKER_NAME = resourceBundle.getString("defaultMarkerName");
		
		String[] colorNames = {
				resourceBundle.getString("red"),
				resourceBundle.getString("green"),
//				resourceBundle.getString("blue"),
				resourceBundle.getString("magenta"),
				resourceBundle.getString("yellow"),
//				resourceBundle.getString("cyan"),
				resourceBundle.getString("orange"),
				resourceBundle.getString("pink"),
				resourceBundle.getString("purple"),
		};
		COLOR_NAMES = colorNames;
		
		org.lgna.story.Color[] colors = { 
				org.lgna.story.Color.RED,
				org.lgna.story.Color.GREEN,
		//				org.lgna.story.Color.BLUE,
				org.lgna.story.Color.MAGENTA,
				org.lgna.story.Color.YELLOW,
		//				org.lgna.story.Color.CYAN,
				org.lgna.story.Color.ORANGE,
				org.lgna.story.Color.PINK,
				org.lgna.story.Color.PURPLE
			};
		COLORS = colors;
	}
	
	private static int getColorIndexForName(String name)
	{
		String lowerName = name.toLowerCase();
		for (int i=0; i<COLOR_NAMES.length; i++)
		{
			String currentColor = COLOR_NAMES[i].toLowerCase();
			if (lowerName.endsWith(currentColor))
			{
				return i;
			}
		}
		return -1;
	}
	
	private static String getIconSuffixForMarkerName(String markerName)
	{
		int colorIndex = getColorIndexForName(markerName);
		if (colorIndex != -1)
		{
			return "_"+COLOR_NAMES[colorIndex]+".png"; 
		}
		else return "_White.png";
	}
	
	public static Icon getIconForCameraMarkerName(String markerName)
	{
		String iconName = "images/markerIcon"+getIconSuffixForMarkerName(markerName);
		return new javax.swing.ImageIcon(MarkerUtilities.class.getResource(iconName));
	}
	
	public static Icon getIconForObjectMarkerName(String markerName)
	{
		String iconName = "images/axis"+getIconSuffixForMarkerName(markerName);
		return new javax.swing.ImageIcon(MarkerUtilities.class.getResource(iconName));
	}
	
	public static org.lgna.story.Color getColorForMarkerName(String markerName)
	{
		int colorIndex = getColorIndexForName(markerName);
		if (colorIndex != -1)
		{
			return COLORS[colorIndex]; 
		}
		else 
		{	
			return null;
		}
	}
	
	private static String makeMarkerName(String baseName, int colorIndex, int addOnNumber)
	{
		String markerName = baseName + "_" + COLOR_NAMES[colorIndex];
		if (addOnNumber > 0)
		{
			markerName += "_"+ Integer.toString( addOnNumber );
		}
		return markerName;
	}
	
	public static String getNameForCameraMarker( NamedUserType ownerType ) {
		MarkerColorValidator nameValidator = new MarkerColorValidator( ownerType );
		int colorIndex = 0;
		int addOnNumber = 0;
		String markerName = makeMarkerName(DEFAULT_CAMERA_MARKER_NAME, colorIndex, addOnNumber);
		while( nameValidator.getExplanationIfOkButtonShouldBeDisabled( markerName ) != null ) {
			colorIndex++;
			if (colorIndex >= COLOR_NAMES.length)
			{
				colorIndex = 0;
				addOnNumber++;
			}
			markerName = makeMarkerName(DEFAULT_CAMERA_MARKER_NAME, colorIndex, addOnNumber);
		}
		return markerName;
	}
	
	public static String getNameForObjectMarker( NamedUserType ownerType, UserField selectedField ) {
		MarkerColorValidator nameValidator = new MarkerColorValidator( ownerType );
		int colorIndex = 0;
		int addOnNumber = 0;
		String markerName = makeMarkerName(selectedField.getName(), colorIndex, addOnNumber);
		while( nameValidator.getExplanationIfOkButtonShouldBeDisabled( markerName ) != null ) {
			colorIndex++;
			if (colorIndex >= COLOR_NAMES.length)
			{
				colorIndex = 0;
				addOnNumber++;
			}
			markerName = makeMarkerName(selectedField.getName(), colorIndex, addOnNumber);
		}
		return markerName;
	}
}

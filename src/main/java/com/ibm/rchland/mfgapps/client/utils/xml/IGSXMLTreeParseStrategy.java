/* @ Copyright IBM Corporation 2008. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2008-02-11   ~1 37616JL  R Prechel        -Initial version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils.xml;

import javax.swing.tree.DefaultMutableTreeNode;

import org.xml.sax.Attributes;

/**
 * <code>IGSXMLTreeParseStrategy</code> is the interface for a strategy class
 * that determines if an XML element corresponds to a tree node, creates an
 * {@link IGSUserObject} for use as the user object of a tree node, and
 * determines if a given tree node should be the initially selected tree node.
 * @author The MFS Client Development Team
 */
public interface IGSXMLTreeParseStrategy
{
	/**
	 * Returns <code>true</code> if the element is the parent element for a
	 * tree node or <code>false</code> if the element is a child element that
	 * contains data for a tree node.
	 * @param element the name of an XML element
	 * @return <code>true</code> if the element is the parent element for a
	 *         tree node or <code>false</code> if the element is a child
	 *         element that contains data for a tree node
	 */
	public boolean isTreeNode(String element);

	/**
	 * Creates the <code>IGSUserObject</code> that will become the user object
	 * of the tree node for the specified XML element.
	 * @param element the name of an XML element that is the parent element for
	 *        a tree node
	 * @param attributes the attributes for the element
	 * @return the <code>IGSUserObject</code>
	 */
	public IGSUserObject createUserObject(String element, Attributes attributes);

	/**
	 * Returns <code>true</code> if the specified <code>node</code> should
	 * be the initially selected tree node.
	 * @param node the <code>DefaultMutableTreeNode</code> to check
	 * @return <code>true</code> if the specified <code>node</code> should
	 *         be the initially selected tree node
	 */
	public boolean isSelectedNode(DefaultMutableTreeNode node);
}

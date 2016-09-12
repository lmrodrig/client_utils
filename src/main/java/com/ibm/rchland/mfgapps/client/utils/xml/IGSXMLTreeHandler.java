/* @ Copyright IBM Corporation 2008, 2011. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2008-02-12     37616JL  R Prechel        -Initial version
 * 2008-03-15  ~1 37616JL  R Prechel        -Implement element replacement
 * 2011-10-20  ~02 00177780Giovanni Toledo  -Added code to avoid duplicates on the tree 
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils.xml;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;


/**
 * <code>IGSXMLTreeHandler</code> parses the XML for an XML based tree using a
 * {@link javax.xml.parsers.SAXParser} and an {@link IGSXMLTreeParseStrategy}.
 * @author The MFS Client Development Team
 */
public class IGSXMLTreeHandler
	extends IGSDefaultHandler
	implements Runnable
{
	/** The <code>InputStream</code> used to read the XML. */
	private final InputStream in;

	/** The XML parsing strategy. */
	private final IGSXMLTreeParseStrategy strategy;

	/** The XML attribute used to indicate the ID of a node. */
	private final String idAttribute; //~1A

	/** The XML element used to perform element replacement. */
	private final String replaceElement; //~1A

	/** The <code>Map</code> of node IDs to nodes. */
	@SuppressWarnings("rawtypes")
	private final Map nodeMap; //~1A

	/** The current node of the tree. */
	private DefaultMutableTreeNode currentNode;

	/** Stores the data accumulated for the current element. */
	private StringBuffer elementData;

	/** Stores any <code>Exception</code> that occurred during parsing. */
	private Exception exception;

	/** The root node of the tree. */
	private DefaultMutableTreeNode rootNode;

	/** The initially selected node of the tree. */
	private DefaultMutableTreeNode selectedNode;

	/**
	 * Constructs a new <code>IGSXMLTreeHandler</code>.
	 * @param in the <code>InputStream</code> used to read the XML
	 * @param strategy the XML parsing strategy
	 */
	public IGSXMLTreeHandler(InputStream in, IGSXMLTreeParseStrategy strategy)
	{
		this(in, strategy, null, null, null);
	}

	//~1A New constructor
	/**
	 * Constructs a new <code>IGSXMLTreeHandler</code>.
	 * @param in the <code>InputStream</code> used to read the XML
	 * @param strategy the XML parsing strategy
	 * @param idAttribute the XML attribute used to indicate the ID of a node
	 * @param replaceElement the XML element used to perform element replacement
	 * @param nodeMap the <code>Map</code> of node IDs to nodes
	 */
	@SuppressWarnings("rawtypes")
	public IGSXMLTreeHandler(InputStream in, IGSXMLTreeParseStrategy strategy,
								String idAttribute, String replaceElement, Map nodeMap)
	{
		super();
		this.in = in;
		this.strategy = strategy;
		this.idAttribute = idAttribute;
		this.replaceElement = replaceElement;
		this.nodeMap = nodeMap;
	}

	/**
	 * Returns the <code>Exception</code> that was thrown during parsing or
	 * <code>null</code> if no <code>Exception</code> was thrown.
	 * @return the <code>Exception</code> or <code>null</code>
	 */
	public Exception getException()
	{
		return this.exception;
	}

	/**
	 * Returns the root <code>DefaultMutableTreeNode</code>.
	 * @return the <code>DefaultMutableTreeNode</code> for the root of the
	 *         tree or <code>null</code> if an error occurred during parsing
	 */
	public DefaultMutableTreeNode getRootNode()
	{
		return this.rootNode;
	}

	/**
	 * Returns the initially selected <code>DefaultMutableTreeNode</code>.
	 * @return the initially selected <code>DefaultMutableTreeNode</code> of
	 *         the tree or <code>null</code> if the tree does not have an
	 *         initially selected node
	 */
	public DefaultMutableTreeNode getSelectedNode()
	{
		return this.selectedNode;
	}

	//~1A New method
	/**
	 * Returns the node with the specified ID.
	 * @param id the ID of a node
	 * @return the node with the specified ID or <code>null</code>
	 */
	public DefaultMutableTreeNode getNode(String id)
	{
		return (DefaultMutableTreeNode) this.nodeMap.get(id);
	}

	/** Parses the XML. */
	public void run()
	{
		try
		{
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setNamespaceAware(true);
			factory.newSAXParser().parse(this.in, this);
		}
		catch (Exception e)
		{
			this.exception = e;
			this.rootNode = null;
			this.selectedNode = null;
		}
	}

	//~1A New method
	/**
	 * Adds the tree represented by <code>tree</code> to <code>parent</code>
	 * by adding a new node to <code>parent</code> for each node in
	 * <code>tree</code>. The new node contains the user object for the
	 * corresponding node in <code>tree</code>. This method is used to
	 * implement element replacement.
	 * @param parent the parent <code>DefaultMutableTreeNode</code>
	 * @param tree the <code>DefaultMutableTreeNode</code> for the root of the
	 *        tree that should be added to <code>parent</code>
	 */
	@SuppressWarnings("rawtypes")
	private static void addTree(DefaultMutableTreeNode parent, Object tree)
	{
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree;
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(node.getUserObject());
		parent.add(newNode);
		Enumeration children = node.children();
		while (children.hasMoreElements())
		{
			addTree(newNode, children.nextElement());
		}
	}

	/**
	 * Receives notification of the start of an element.
	 * @param uri the namespace URI
	 * @param localName the local name of the element (without namespace prefix)
	 * @param qName the qualified name of the element (with namespace prefix)
	 * @param attributes the attributes for the element
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void startElement(String uri, String localName, String qName,
								Attributes attributes)
	{
		String elementName = getElementName(localName, qName);
		if (this.strategy.isTreeNode(elementName))
		{
			IGSUserObject obj = this.strategy.createUserObject(elementName, attributes);
			
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(obj, true);
			if (this.rootNode == null)
			{
				this.rootNode = node;
			}
			else
			{
				/** ~02A Start*/
				boolean findNode = false; 

				if(this.currentNode.getChildCount() > 0)
				{
					Enumeration children = this.currentNode.children();
					DefaultMutableTreeNode tempNode = null;

					while(children.hasMoreElements())
					{
						tempNode = (DefaultMutableTreeNode)children.nextElement();
						IGSUserObject tempObj = (IGSUserObject)tempNode.getUserObject();
						if(tempObj == obj)
						{
							node = tempNode;
							findNode = true;
							break;
						}
					}
				}

				if(!findNode)
				{
					this.currentNode.add(node);
				}
				/** ~02A End */
			}
			this.currentNode = node;

			//~1A Map the node to its ID
			if (this.idAttribute != null)
			{
				String id = attributes.getValue(this.idAttribute);
				if (id != null)
				{
					this.nodeMap.put(id, node);
				}
			}
		}
		else
		{
			this.elementData = new StringBuffer();
		}
	}

	/**
	 * Receives notification of the end of an element.
	 * @param uri the namespace URI
	 * @param localName the local name of the element (without namespace prefix)
	 * @param qName the qualified name of the element (with namespace prefix)
	 */
	public void endElement(String uri, String localName, String qName)
	{
		String elementName = getElementName(localName, qName);
		if (this.strategy.isTreeNode(elementName))
		{
			if (this.strategy.isSelectedNode(this.currentNode))
			{
				this.selectedNode = this.currentNode;
			}
			this.currentNode = (DefaultMutableTreeNode) this.currentNode.getParent();
		}
		else if (this.currentNode != null)
		{
			//~1A Do element replacement if elementName equals replaceElement
			String data = this.elementData.toString();
			if (elementName.equals(this.replaceElement))
			{
				if (this.nodeMap.containsKey(data))
				{
					addTree(this.currentNode, this.nodeMap.get(data));
				}
			}
			else
			{
				Object obj = this.currentNode.getUserObject();
				if (obj instanceof IGSUserObject)
				{
					((IGSUserObject) obj).add(elementName, data);
				}
				else if (obj != null)
				{
					this.currentNode.setUserObject(obj + data);
				}
				else
				{
					this.currentNode.setUserObject(data);
				}
			}
		}
	}

	/**
	 * Receives notification of character data inside an element.
	 * @param ch the characters
	 * @param start the start position in the character array
	 * @param length the number of characters to use from the character array
	 */
	public void characters(char[] ch, int start, int length)
	{
		String data = new String(ch, start, length);
		this.elementData.append(data);
	}

	/**
	 * Receives notification of ignorable whitespace inside an element.
	 * @param ch the characters
	 * @param start the start position in the character array
	 * @param length the number of characters to use from the character array
	 */
	public void ignorableWhitespace(char[] ch, int start, int length)
	{
		characters(ch, start, length);
	}
}

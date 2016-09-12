/* @ Copyright IBM Corporation 2007-2014. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR 	Name             Details
 * ---------- ---- ------------ ---------------- ----------------------------------------------
 * 2008-01-12      39619JL   	R Prechel        -Initial version
 * 2008-03-15   ~1 37616JL  	R Prechel        -Handle null and zero length data.
 *                                    	 	     -Add toXMLString so toString can be
 *                                          	  overridden to create debug Strings.
 * 2009-03-23   ~2 43907MC  	Santiago SC      -Add getNextReqElement and stepIntoReqElement
 * 2009-03-24   ~3 43907MC  	Jaime G.         -Add startDocument with encoding value. 
 * 2014-02-28	~4 RCQ00281756	Cesar M.		 -Add getFieldCount and fieldExists
 *********************************************************************************************/
package com.ibm.rchland.mfgapps.client.utils.xml;

import java.util.Collection;
import java.util.Iterator;
import java.util.Stack;

import com.ibm.rchland.mfgapps.client.utils.exception.IGSException;

/**
 * <code>IGSXMLDocument</code> provides methods to build and parse XML
 * documents.
 * @author The Process Profile Client Development Team
 */
public class IGSXMLDocument
{
	/** The <code>StringBuffer</code> used to build the XML document. */
	protected final StringBuffer buffer = new StringBuffer();

	/** The <code>Stack</code> of {@link IGSXMLBoundary boundaries}. */
	@SuppressWarnings("rawtypes")
	protected final Stack boundaries = new Stack();

	/** Set <code>true</code> when a start tag is open. */
	protected boolean startTagOpen = false;

	/** Constructs a new <code>IGSXMLDocument</code>. */
	@SuppressWarnings("unchecked")
	public IGSXMLDocument()
	{
		super();
		this.boundaries.push(new IGSXMLBoundary(0, Integer.MAX_VALUE));
	}

	/**
	 * Constructs a new <code>IGSXMLDocument</code>.
	 * @param xml the XML for the document
	 */
	public IGSXMLDocument(String xml)
	{
		this();
		this.buffer.append(xml);
	}

	/**
	 * Finds the index of the first occurrence of an element with the given name
	 * starting at the specified offset.
	 * @param name the name of the element
	 * @param xml the XML to parse
	 * @param offset the starting offset into the XML
	 * @return the index of the first occurrence of the element or -1 if the
	 *         element was not found
	 */
	public static int findStartTag(String name, StringBuffer xml, int offset)
	{
		int indices[] = new int[3];
		indices[0] = xml.indexOf('<' + name + ' ', offset);
		indices[1] = xml.indexOf('<' + name + '/', offset);
		indices[2] = xml.indexOf('<' + name + '>', offset);

		int result = Integer.MAX_VALUE;
		for (int i = 0; i < indices.length; i++)
		{
			if (indices[i] != -1 && indices[i] < result)
			{
				result = indices[i];
			}
		}

		return result == Integer.MAX_VALUE ? -1 : result;
	}

	/**
	 * Returns the data of the first element with the given name inside the
	 * specified boundary.
	 * @param name the name of the element
	 * @param xml the XML to parse
	 * @param boundary the XML document parsing boundary
	 * @return the data of the first element with the given name or
	 *         <code>null</code> if no such element exists
	 */
	public static String getElement(String name, StringBuffer xml, IGSXMLBoundary boundary)
	{
		String result = null;
		int soIndex = findStartTag(name, xml, boundary.endOffset);
		if (soIndex != -1)
		{
			int scIndex = xml.indexOf(">", soIndex); //$NON-NLS-1$
			if (scIndex != -1)
			{
				//If empty element
				if (xml.charAt(scIndex - 1) == '/')
				{
					if (scIndex <= boundary.end)
					{
						boundary.startOffset = soIndex;
						boundary.endOffset = scIndex + 1;
						result = ""; //$NON-NLS-1$
					}
				}
				//If non-empty element
				else
				{
					String endTag = "</" + name + '>'; //$NON-NLS-1$
					int eIndex = xml.indexOf(endTag, scIndex);
					if (eIndex != -1 && eIndex <= boundary.end)
					{
						boundary.startOffset = soIndex;
						boundary.endOffset = eIndex + endTag.length();
						return xml.substring(scIndex + 1, eIndex);
					}
				}
			}
		}
		return result;
	}

	/** Clears the XML document. */
	public void clear()
	{
		this.buffer.setLength(0);
	}

	/**
	 * Returns the length of the XML document.
	 * @return the length of the XML document
	 */
	public int getLength()
	{
		return this.buffer.length();
	}

	/** Resets the current XML document parsing boundary. */
	public void resetCurrentBoundary()
	{
		IGSXMLBoundary boundary = (IGSXMLBoundary) this.boundaries.peek();
		boundary.startOffset = boundary.start;
		boundary.endOffset = boundary.start;
	}

	/**
	 * Returns a <code>String</code> representation of the XML document.
	 * <p>
	 * Note: The {@link #toXMLString()} method should be used instead of this
	 * method to obtain the XML for this document, as this method may be
	 * overridden.
	 * @return an XML <code>String</code>
	 */
	public String toString()
	{
		return toXMLString();
	}

	//~1A New method
	/**
	 * Returns a <code>String</code> representation of the XML document.
	 * @return an XML <code>String</code>
	 */
	public final String toXMLString()
	{
		return this.buffer.toString();
	}

	/** Starts the XML document. */
	public void startDocument()
	{
		this.buffer.setLength(0);
		this.buffer.append("<?xml version=\"1.0\"?>"); //$NON-NLS-1$
	}
	
	//~3A
	/** Starts the XML document. */
	public void startDocument(String encoding)
	{
		this.buffer.setLength(0);
		this.buffer.append("<?xml version=\"1.0\" encoding=\""+encoding+"\"?>"); //$NON-NLS-1$
	}
	/** Closes a start tag if one is open. */
	protected void closeStartTagIfNecessary()
	{
		if (this.startTagOpen)
		{
			this.buffer.append('>');
			this.startTagOpen = false;
		}
	}

	/**
	 * Adds a complete element to the XML document.
	 * @param name the name of the element
	 * @param data the data for the element
	 */
	public void addElement(String name, Object data)
	{
		/* ~1C Update to correctly handle null and zero length data. */
		String string = null;
		if (data == null || (string = data.toString()).length() == 0)
		{
			addEmptyElement(name);
		}
		else
		{
			closeStartTagIfNecessary();
			this.buffer.append('<');
			this.buffer.append(name);
			this.buffer.append('>');

			this.buffer.append(string);

			this.buffer.append("</"); //$NON-NLS-1$
			this.buffer.append(name);
			this.buffer.append('>');
		}
	}

	/**
	 * Adds a complete empty element to the XML document.
	 * @param name the name of the element
	 */
	public void addEmptyElement(String name)
	{
		closeStartTagIfNecessary();
		this.buffer.append('<');
		this.buffer.append(name);
		this.buffer.append("/>"); //$NON-NLS-1$
	}

	/**
	 * Starts a new element in the XML document.
	 * @param name the name of the element
	 */
	public void startElement(String name)
	{
		closeStartTagIfNecessary();
		this.buffer.append('<');
		this.buffer.append(name);
		this.startTagOpen = true;
	}

	/**
	 * Adds an attribute to the XML document.
	 * @param name the name of the attribute
	 * @param value the value of the attribute
	 * @throws IllegalStateException if the start tag is closed
	 */
	public void addAttribute(String name, Object value)
	{
		if (this.startTagOpen == false)
		{
			throw new IllegalStateException(
					"Method must be called before start tag is closed."); //$NON-NLS-1$
		}

		this.buffer.append(' ');
		this.buffer.append(name);
		this.buffer.append("=\""); //$NON-NLS-1$
		this.buffer.append(value);
		this.buffer.append('"');
	}

	/**
	 * Adds element data to the XML document.
	 * @param data the data
	 */
	public void addData(Object data)
	{
		/* ~1C Update to correctly handle null and zero length data. */
		String string = null;
		if (data != null && (string = data.toString()).length() != 0)
		{
			closeStartTagIfNecessary();
			this.buffer.append(string);
		}
	}

	/**
	 * Ends an element in the XML document.
	 * @param name the name of the element
	 */
	public void endElement(String name)
	{
		if (this.startTagOpen)
		{
			this.buffer.append("/>"); //$NON-NLS-1$
			this.startTagOpen = false;
		}
		else
		{
			this.buffer.append("</"); //$NON-NLS-1$
			this.buffer.append(name);
			this.buffer.append('>');
		}
	}

	/** Ends the XML document. */
	public void endDocument()
	{
		//Does nothing
	}

	/**
	 * Returns the data of the first element with the given name.
	 * @param name the name of the element
	 * @return the data of the first element with the given name or
	 *         <code>null</code> if no such element exists
	 */
	public String getFirstElement(String name)
	{
		IGSXMLBoundary boundary = (IGSXMLBoundary) this.boundaries.peek();
		boundary.startOffset = boundary.start;
		boundary.endOffset = boundary.start;
		return getElement(name, this.buffer, boundary);
	}

	/**
	 * Returns the data of the next element with the given name.
	 * @param name the name of the element
	 * @return the data of the next element with the given name or
	 *         <code>null</code> if no such element exists
	 */
	public String getNextElement(String name)
	{
		IGSXMLBoundary marker = (IGSXMLBoundary) this.boundaries.peek();
		return getElement(name, this.buffer, marker);
	}

	//~2A
	/**
	 * Returns the data of the next required element with the given name.
	 * @param name the name of the required element
	 * @return the data of the next required element with the given name.
	 * @throws an IGSException if the <code>String</code> name does not exists.
	 */
	public String getNextReqElement(String name) throws IGSException
	{
		IGSXMLBoundary marker = (IGSXMLBoundary) this.boundaries.peek();
		String element = getElement(name, this.buffer, marker);
		if(element == null)
		{
		    throw new IGSException("No <" + name + "> tags were found!");
		}
		else if(element.trim().equals(""))
		{
		    throw new IGSException("Required value for tag <" + name + "> is empty!");
		}
		return element;
	}	
	
	/**
	 * Finds the next element with the given name and pushes a new boundary onto
	 * the document's stack of boundaries such that the XML parsing methods will
	 * only parse the element's data.
	 * @param name the name of the element
	 * @return the data of the next element with the given name or
	 *         <code>null</code> if no such element exists
	 */
	@SuppressWarnings("unchecked")
	public String stepIntoElement(String name)
	{
		IGSXMLBoundary marker = (IGSXMLBoundary) this.boundaries.peek();
		String element = getElement(name, this.buffer, marker);
		if (element != null)
		{
			this.boundaries.push(new IGSXMLBoundary(marker.startOffset, marker.endOffset));
		}
		return element;
	}
	
	//~2A
	/**
	 * Finds the next element with the given name and pushes a new 
	 * boundary onto the document's stack of boundaries such that the XML 
	 * parsing methods will only parse the element's data.
	 * @param name the name of the required element
	 * @return the data of the next rquired element with the given name.
	 * @throws an IGSException if the <code>String</code> name does 
	 * not exists.
	 */
	@SuppressWarnings("unchecked")
	public String stepIntoReqElement(String name) throws IGSException
	{
		IGSXMLBoundary marker = (IGSXMLBoundary) this.boundaries.peek();
		String element = getElement(name, this.buffer, marker);
		if (element != null)
		{
			this.boundaries.push(new IGSXMLBoundary(marker.startOffset, marker.endOffset));
		}
		else
		{
		    throw new IGSException("No <" + name + "> tags were found!");		    
		}
		return element;
	}

	/**
	 * Pops the last boundary pushed onto the stack of boundaries by the
	 * {@link #stepIntoElement(String)} method and sets the current boundary
	 * such that parsing will continue with the element following the element
	 * that was parsed by the {@link #stepIntoElement(String)} function.
	 * @return <code>true</code> if a boundary was popped from the stack of
	 *         boundaries; <code>false</code> if a boundary was not popped
	 *         from the stack of boundaries because the initial boundary is at
	 *         the top of the stack
	 */
	public boolean stepOutOfElement()
	{
		if (this.boundaries.size() > 1)
		{
			this.boundaries.pop();
			return true;
		}
		return false;
	}

	/**
	 * Adds a collection of child elements wrapped by a parent element.
	 * @param iterator an <code>Iterator</code> for the collection of elements
	 * @param parent the name of the parent element used to wrap the collection
	 *        of child elements
	 * @param child the name of a child element
	 */
	@SuppressWarnings("rawtypes")
	public void addElements(Iterator iterator, String parent, String child)
	{
		startElement(parent);
		while (iterator.hasNext())
		{
			addElement(child, iterator.next());
		}
		endElement(parent);
	}

	/**
	 * Parses a collection of child elements wrapped by a parent element.
	 * @param collection the <code>Collection</code> used to store the parsed
	 *        element data
	 * @param parent the name of the parent element used to wrap the collection
	 *        of child elements
	 * @param child the name of a child element
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void getElements(Collection collection, String parent, String child)
	{
		String element;
		if (stepIntoElement(parent) != null)
		{
			while ((element = getNextElement(child)) != null)
			{
				collection.add(element);
			}
			stepOutOfElement();
		}
	}

	/**
	 * <code>IGSXMLBoundary</code> is used to establish the boundaries for the
	 * XML parsing methods. The XML parsing methods will only find an element
	 * that is within the current boundary. The initial boundary encompasses the
	 * entire XML document. Each subsequent boundary is a subset of its parent
	 * boundary. A {@link java.util.Stack} of boundaries is maintained by an
	 * {@link IGSXMLDocument} and the current boundary is at the top of the
	 * stack. The {@link IGSXMLDocument#stepIntoElement(String)} method pushes a
	 * boundary onto the stack and the {@link IGSXMLDocument#stepOutOfElement()}
	 * method pops a boundary from the stack.
	 * @author The Process Profile Client Development Team
	 */
	public static class IGSXMLBoundary
	{
		/** The start index of the boundary. */
		public final int start;

		/** The starting offset of the last element parsed. */
		public int startOffset;

		/** The ending offset of the last element parsed. */
		public int endOffset;

		/** The end index of the boundary. */
		public final int end;

		/**
		 * Constructs a new <code>IGSXMLMarker</code>.
		 * @param start the start index of the boundary
		 * @param end the end index of the boundary
		 */
		public IGSXMLBoundary(int start, int end)
		{
			this.start = start;
			this.startOffset = start;
			this.endOffset = start;
			this.end = end;
		}
	}
	
	//~4A
	/**
	 * Returns the number of occurrences of the given field name string.
	 * @param The name of the field to count
	 * @return The amount of occurrences of the field
	 */

	public int getFieldCount(String field)
	{
		int count = 0;
		int index = 0;

		field = "<" + field + ">";
		int incrementSize = field.length();

		while (index < buffer.length() && buffer.indexOf(field, index) != -1)
		{
			count++;
			index = buffer.indexOf(field, index) + incrementSize;
		}

		return count;
	}
	
	//~4A
	/**
	 * Returns true if the field exists in the buffer XML	
	 * @param The name of the field to count
	 * @return true if found. Otherwise, false
	 */

	public boolean fieldExists(String fieldName)
	{
		if (buffer.toString().indexOf("<" + fieldName + ">") != -1)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Finds the index of the first occurrence of an element with the given name
	 * starting at the current index.
	 * @param name the name of the element
	 * @return the index of the first occurrence of the element or -1 if the
	 *         element was not found
	 */
	public int findStartTagFromCurr(String name)
	{
		IGSXMLBoundary marker = (IGSXMLBoundary) this.boundaries.peek();
		
		return findStartTag(name,this.buffer,marker.endOffset);
	}
}

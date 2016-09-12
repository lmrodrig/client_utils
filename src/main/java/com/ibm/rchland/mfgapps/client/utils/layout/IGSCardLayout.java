/* @ Copyright IBM Corporation 2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2010-02-11       45878MS Santiago SC      -Initial version, Java 5.0
 ******************************************************************************/

package com.ibm.rchland.mfgapps.client.utils.layout;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.util.LinkedList;

/**
 * <code>IGSCardLayout</code> is a variant of <code>CardLayout</code> that
 * has a getter to obtain the current <code>Component</code> that is displayed.
 * @author The MFS Client Development Team
 */
public class IGSCardLayout extends CardLayout 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4609273651026143631L;

	/** List of component identifier in the layout */
	private LinkedList<String> compIdList = new LinkedList<String>();
			
	/** List of <code>Component</code>s in the layout*/
	private LinkedList<Component> compList = new LinkedList<Component>();
	
	/** Index in the list of the <code>Component</code> displayed */
	private int index = -1;
	
	@Override
	/**
	 * {@inheritDoc}
	 */
	public void addLayoutComponent(Component comp, Object constraints)
	{
		super.addLayoutComponent(comp, constraints);
				
		compList.add(comp);
		compIdList.add(constraints.toString());
		
		if(1 == compList.size())
		{
			setCurrentComponentIndex(0);
		}
	}
	
	@Override
	/**
	 * {@inheritDoc}
	 */
	public void first(Container parent)
	{
		super.first(parent);
		
		if(!compList.isEmpty())
		{
			setCurrentComponentIndex(0);
		}
	}
	
	/**
	 * Get the currently displayed <code>Component</code>
	 * @return the <code>Component</code>
	 */
	public Component getCurrentComponent()
	{
		return compList.get(index);
	}
	
	/**
	 * Get the currently displayed <code>Component</code> index
	 * @return the index
	 */
	private int getCurrentComponentIndex()
	{
		return index;
	}
	
	@Override
	/**
	 * {@inheritDoc}
	 */
	public void last(Container parent)
	{
		super.last(parent);
		
		if(!compList.isEmpty())
		{
			setCurrentComponentIndex(compList.size() - 1);
		}
	}
	
	@Override
	/**
	 * {@inheritDoc}
	 */
	public void next(Container parent)
	{
		super.next(parent);
		
		if(!compList.isEmpty())
		{
			setCurrentComponentIndex(1 + getCurrentComponentIndex());
		}
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public void previous(Container parent)
	{
		super.preferredLayoutSize(parent);
		
		if(!compList.isEmpty())
		{
			setCurrentComponentIndex(getCurrentComponentIndex() - 1);
		}
	}
	
	@Override
	/**
	 * {@inheritDoc}
	 */
	public void removeLayoutComponent(Component comp)
	{
		super.removeLayoutComponent(comp);
		
		for(int compNum = 0; compNum < compList.size(); compNum++)
		{
			if(comp.equals(compList.get(compNum)))
			{
				int index = getCurrentComponentIndex();
				
				// if the current component is removed
				if(index == compNum)
				{
					// if the component is the only one in the list
					if(index == 0 && 1 == compList.size())
					{
						index = -1;						
					}
					// if is NOT the first and the list has more components
					else if(0 < index)
					{
						index--;
					}		
					// else is the first and the list has more components, leave the index as is = 0
				}
				// if a component before the current component is removed
				else if(compNum < index)
				{
					index--;
				}
				// else the removed component is after the current, leave the index as is 
												
				compList.remove(compNum);
				compIdList.remove(compNum);
				
				setCurrentComponentIndex(index);
				
				break;
			}
		}
	}
	
	/**
	 * Sets/updates the index of the <code>Component</code>s
	 * @param index to be set
	 */
	private void setCurrentComponentIndex(int index)
	{
		int comps = compList.size();
		
		this.index = (0 > index)? comps - 1 : (index == comps)? 0 : index;
	}
	
	@Override
	/**
	 * {@inheritDoc}
	 */
	public void show(Container parent, String id)
	{
		super.show(parent, id);
		
		for(int compNum = 0; compNum < compList.size(); compNum++)
		{
			if(id.equals(compIdList.get(compNum)))
			{
				setCurrentComponentIndex(compNum);
				
				break;
			}
		}
	}
}

package org.gsfan.clustermonitor.mainframe;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class TabsContent extends Component{

	private String tabsName = null;
	private JScrollPane scrollPane = null ;
	
	public TabsContent(String tabsName, JComponent component){
		this.tabsName = tabsName;

		scrollPane = new JScrollPane(component);
//		scrollPane.setBackground(Color.white);
	}
	public String getTabsName() {
		return tabsName;
	}

	public void setTabsName(String tabsName) {
		this.tabsName = tabsName;
	}
	public void setScrollPane(JScrollPane scrollPane) {
		this.scrollPane = scrollPane;
	}
	public JScrollPane getScrollPane(){
		return this.scrollPane;
	}
}

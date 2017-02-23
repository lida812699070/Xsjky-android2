package com.jq.printer;

public class JQPrinterInfo {
	
	public final static int STATE_NOPAPER_UNMASK = 0x01;
	public final static int STATE_OVERHEAT_UNMASK = 0x02;
	public final static int STATE_BATTERYLOW_UNMASK = 0x04;
	public final static int STATE_PRINTING_UNMASK = 0x08;
	public final static int STATE_COVEROPEN_UNMASK = 0x10;
	public JQPrinterInfo()
	{
	}

	public void stateReset()
	{
		isNoPaper = false;
		isOverHeat = false;
		isBatteryLow = false;
		isPrinting = false; 
		isCoverOpen = false;
	}
	public boolean isNoPaper;

	public boolean isOverHeat;
	public boolean isBatteryLow;
	
	public boolean isPrinting;
	
	public boolean isCoverOpen;
}

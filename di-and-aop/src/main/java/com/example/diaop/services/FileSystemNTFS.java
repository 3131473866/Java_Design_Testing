package com.example.diaop.services;

import com.example.diaop.interfaces.FileSystem;

public class FileSystemNTFS implements FileSystem {
	@Override
	public String getType() {
		return "NTFS FileSystem";
	}
	
	@Override
	public boolean run() {
		System.out.println("running " + this.getType());
		
		// invoke services here if applicable
		
		return true;
	}
}

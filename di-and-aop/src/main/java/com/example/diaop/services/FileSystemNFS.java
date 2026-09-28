package com.example.diaop.services;

import com.example.diaop.interfaces.FileSystem;

public class FileSystemNFS implements FileSystem {
	@Override
	public String getType() {
		return "NFS FileSystem";
	}
	
	@Override
	public boolean run() {
		System.out.println("running " + this.getType());
		
		// invoke services here if applicable
		
		return true;
	}
}

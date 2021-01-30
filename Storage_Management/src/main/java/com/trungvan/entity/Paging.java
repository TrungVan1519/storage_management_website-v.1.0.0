package com.trungvan.entity;

public class Paging {

	private long totalRows;
	private int totalPages;
	private int indexPage;	// > Trang hien tai
	private int recordPerPage = 10;
	private int offset;		// > STT khi query: 0-9, 10-19, 20-29, ...
	
	public Paging(int recordPerPage) {
		
		this.recordPerPage = recordPerPage;
	}

	public long getTotalRows() {
		
		return totalRows;
	}

	public void setTotalRows(long totalRows) {
		
		this.totalRows = totalRows;
	}
	
	public int getTotalPages() {
		
		if(totalRows > 0) {
			
			totalPages = (int) Math.ceil(totalRows / (double) recordPerPage);
		}
		
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		
		this.totalPages = totalPages;
	}

	public int getIndexPage() {
		
		return indexPage;
	}

	public void setIndexPage(int indexPage) {
		
		this.indexPage = indexPage;
	}

	public int getRecordPerPage() {
		
		return recordPerPage;
	}

	public void setRecordPerPage(int recordPerPage) {
		
		this.recordPerPage = recordPerPage;
	}
	
	public int getOffset() {
		
		if(indexPage > 0) {
			
			offset = indexPage * recordPerPage - recordPerPage;	// > 1*10 - 10 = 0, 2*10 - 10 = 10, ...
		}
		return offset;
	}

	public void setOffset(int offset) {
		
		this.offset = offset;
	}
}

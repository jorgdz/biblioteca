package com.github.com.jorgdz.app.util.paginator;

import java.util.List;

public class PageResponse {
	
	private List<?> data;
	
	private int firstPage;
	
	private int lastPage;
	
	private int numberCurrentPage;
	
	private int previousPage;
	
	private int nextPage;
	
	private Long totalElements;
		
	private boolean first;
	
	private boolean last; 
	
	private List<Integer> numbers;

	
	public PageResponse(List<?> data, int firstPage, int lastPage, int numberCurrentPage, int previousPage, int nextPage,
			Long totalElements, boolean first, boolean last, List<Integer> numbers) {
		super();
		this.data = data;
		this.firstPage = firstPage;
		this.lastPage = lastPage;
		this.numberCurrentPage = numberCurrentPage;
		this.previousPage = previousPage;
		this.nextPage = nextPage;
		this.totalElements = totalElements;
		this.first = first;
		this.last = last;
		this.numbers = numbers;
	}

	public PageResponse() {
		super();
	}

	public List<?> getData() {
		return data;
	}

	public void setData(List<?> data) {
		this.data = data;
	}

	public int getFirstPage() {
		return firstPage;
	}

	public void setFirstPage(int firstPage) {
		this.firstPage = firstPage;
	}

	public int getLastPage() {
		return lastPage;
	}

	public void setLastPage(int lastPage) {
		this.lastPage = lastPage;
	}

	public int getNumberCurrentPage() {
		return numberCurrentPage;
	}

	public void setNumberCurrentPage(int numberCurrentPage) {
		this.numberCurrentPage = numberCurrentPage;
	}

	public int getPreviousPage() {
		return previousPage;
	}

	public void setPreviousPage(int previousPage) {
		this.previousPage = previousPage;
	}

	public int getNextPage() {
		return nextPage;
	}

	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}

	public Long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(Long totalElements) {
		this.totalElements = totalElements;
	}

	public boolean isFirst() {
		return first;
	}

	public void setFirst(boolean first) {
		this.first = first;
	}

	public boolean isLast() {
		return last;
	}

	public void setLast(boolean last) {
		this.last = last;
	}

	public List<Integer> getNumbers() {
		return numbers;
	}

	public void setNumbers(List<Integer> numbers) {
		this.numbers = numbers;
	}
	
}

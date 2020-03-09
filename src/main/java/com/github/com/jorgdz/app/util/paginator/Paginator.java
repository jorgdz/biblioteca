package com.github.com.jorgdz.app.util.paginator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

public class Paginator <T> {
	
	private Page<T> pages;
	
	private int first;
	
	private  int last;
	
	public Paginator (Page<T> pages)
	{
		this.pages = pages;
	}
	
	public Paginator (){}
		
	public PageResponse paginate()
	{
		PageResponse pageResponse = new PageResponse(this.pages.getContent(), 0, this.lastPage(), this.numberCurrentPage(), 
				this.previousPage(), this.nextPage(), this.pages.getTotalElements(), this.pages.isFirst(), this.pages.isLast(), this.getNumbers());
		
		return pageResponse;
	}
	
	
	public List<Integer> getNumbers ()
	{
		List<Integer> numbers = new ArrayList<Integer>();
		
		if (this.pages.getSize() >= this.pages.getTotalPages())
		{
			this.first = 1;
			this.last = this.pages.getTotalPages();
		}
		else
		{
			this.first = this.numberCurrentPage() + 1;
			
			if ((this.pages.getSize() + this.numberCurrentPage() + 1)  <=  this.pages.getTotalPages())
			{
				this.last= this.pages.getSize() + this.numberCurrentPage() + 1;
			}
			else
			{
				this.first = this.pages.getTotalPages() - this.pages.getSize();
				this.last = this.pages.getTotalPages();
			}
		}
		
		for (int i = this.first; i <= this.last; i++) 
		{
			numbers.add(i);
		}
		
		return numbers;
	}
	
	
	public int nextPage ()
	{
		return (this.pages.getNumber() < (this.pages.getTotalPages() - 1)) ? this.pages.getNumber() + 1 : this.pages.getNumber();
	}
	
	public int previousPage ()
	{
		return (this.pages.getNumber() > 0) ? this.pages.getNumber() - 1 : this.pages.getNumber();
	}
	
	public int lastPage ()
	{
		return (this.pages.getTotalPages() == 0) ? this.pages.getTotalPages() : (this.pages.getTotalPages() - 1);
	}
	
	public int numberCurrentPage ()
	{
		return this.pages.getNumber();
	}
}

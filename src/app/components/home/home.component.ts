import { Component, OnInit } from '@angular/core';
import { Product } from '../../models/product';
import { ProductService } from '../../services/product/product.service';
import { envipronment } from '../../environments/environment';
import { TokenService } from '../../services/token/token.service';
import { CategoryService } from '../../services/category/category.service';
import { Category } from '../../models/category';
import { Router } from '@angular/router';
import { ProductDetailComponent } from '../product-detail/product-detail.component';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit {
  products: Product[] = [];
  totalPages: number = 0;
  
  currentPage: number = 1;
  itemsPage: number = 20;
  search: string = '';
  category_id: number = 0;
  pages: number[] = [];
  visiblePages: number[] = [];

  categories: Category[] = [];
  selectedCategory: string = '0';
  

  constructor(private productService: ProductService, private categoryService: CategoryService, private router:Router) {
  }

  ngOnInit(): void {
    this.getCategories();
    this.getProducts(this.currentPage, this.itemsPage, this.search, this.category_id);
  }

  getProducts (page:number, limit:number, search: string, category_id:number) {
    this.category_id = Number(this.selectedCategory);
    this.productService.getProducts(page, limit, search, category_id).subscribe({
      next: (response: any) => {
        response.products.forEach((product:Product) => {
          product.thumbnail = product.thumbnail ? `${envipronment.apiPrefix}/products/images/${product.thumbnail}` : '../../../assets/images/image-not-found-icon.png';
        });

        this.products = response.products;
        this.totalPages = response.totalPages;
        this.visiblePages = this.generateVisiblePageArray(this.currentPage, this.totalPages);
      },
      complete: () => {
        
      },
      error: (error: any) => {
        alert(error.error.message);
      }
    });
  }

  onPageChange (page: number) {
    this.currentPage = page;
    this.getProducts(this.currentPage, this.itemsPage, this.search, this.category_id);
  }

  generateVisiblePageArray (currentPage: number, totalPages: number): number[] {
    const maxVisiblePages = 5;
    const halfVisiblePages = Math.floor(maxVisiblePages / 2);

    let startPage = Math.max(currentPage - halfVisiblePages, 1);
    let endPage = Math.min(startPage + maxVisiblePages - 1, totalPages);

    if (endPage - startPage + 1 < maxVisiblePages) {
      startPage = Math.max(endPage - maxVisiblePages + 1, 1);
    }

    return new Array(endPage - startPage + 1).fill(0).map((valueAny, index) => startPage + index);
  }

  getCategories() {
    this.categoryService.getCategories().subscribe({
      next: (response: Category[]) => {
        this.categories = response;
      },
      complete: () => {

      },
      error: (error) => {
        alert(error.error.message);
      }
    });
  }

  searchProducts () {
    this.category_id = Number(this.selectedCategory);
    this.getProducts(1, 10, this.search, this.category_id);
    this.currentPage = 1;
  }
}

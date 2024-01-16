import { Component, OnInit } from '@angular/core';
import { Product } from '../../models/product';
import { ProductService } from '../../services/product/product.service';
import { envipronment } from '../../environments/environment';
import { TodoService } from '../../todo.service';
import { TokenService } from '../../services/token/token.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit {
  products: Product[] = [];
  totalPages: number = 0;
  
  currentPage: number = 1;
  itemsPage: number = 10;
  pages: number[] = [];
  visiblePages: number[] = [];

  constructor(private productService: ProductService, private todoService: TodoService, private tokenService: TokenService) {
    // this.tokenService.removeToken();
    // this.tokenService.setToken('aaa');
    // console.log("Token: "+this.tokenService.getToken());

    // this.todoService.getData().subscribe(data => {
    //   console.warn(data);
    // });

    // this.productService.getProducts(1, 10).subscribe(data => {
    //   console.log(this.tokenService.getToken());
    //   console.warn(data);
    // });
  }

  ngOnInit(): void {
    this.getProducts(this.currentPage, this.itemsPage);
  }

  getProducts (page:number, limit:number) {
    page = page - 1;
    this.productService.getProducts(page, limit).subscribe({
      next: (response: any) => {
        response.products.forEach((product:Product) => {
          product.thumbnail = `${envipronment.apiPrefix}/products/images/${product.thumbnail}`;
          // alert(product.thumbnail);
        });

        this.products = response.products;
        this.totalPages = response.totalPages;
        this.visiblePages = this.generateVisiblePageArray(this.currentPage, this.totalPages);
        console.log(response);
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
    this.getProducts(this.currentPage, this.itemsPage);
  }

  generateVisiblePageArray (currentPage: number, totalPages: number): number[] {
    const maxVisiblePages = 5;
    const halfVisiblePages = Math.floor(maxVisiblePages / 2);

    let startPage = Math.max(currentPage - halfVisiblePages, 1);
    let endPage = Math.min(startPage + maxVisiblePages - 1, totalPages);

    if (endPage - startPage + 1 < maxVisiblePages) {
      startPage = Math.max(endPage - maxVisiblePages + 1, 1);
    }

    return new Array(endPage - startPage + 1).fill(0).map((_, index) => startPage + index);
  }
}

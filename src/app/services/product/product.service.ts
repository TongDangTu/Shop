import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { envipronment } from '../../environments/environment';
import { Observable } from 'rxjs';
import { Product } from '../../models/product';
import { ProductImage } from '../../models/product-image';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  productUrl = `${envipronment.apiPrefix}/products`;
  httpHeaders = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor (private httpClient:HttpClient) {

  }

  getProducts (page: number, limit: number, search: string, category_id:number): Observable<Product[]> {
    page = page - 1;
    // const params = new HttpParams()
    //   .set('page', page.toString())
    //   .set('limit', limit.toString());
    // return this.httpClient.get<Product[]>(`${this.productUrl}`, {params});

    // hoặc
    const url = `?page=${page}&limit=${limit}&search=${search}&category_id=${category_id}`;
    return this.httpClient.get<Product[]>(`${this.productUrl}${url}`);
  }

  getProduct (id: number) : Observable<Product> {
    return this.httpClient.get<Product>(`${this.productUrl}/${id}`);
  }

  getProductImagesByProductId (id: number): Observable<ProductImage[]> {
    const url = `/${id}/images`;
    return this.httpClient.get<ProductImage[]>(`${this.productUrl}${url}`);
  }

  getProductsByProductIds (ids: number[]): Observable<Product[]> {
    const url = `/ids`;
    console.log("${this.productUrl}${url}", `${this.productUrl}${url}`);
    console.log("ids", ids);
    return this.httpClient.post<Product[]>(`${this.productUrl}${url}`, ids, this.httpHeaders);
  }
}
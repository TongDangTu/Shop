import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { envipronment } from '../../environments/environment';
import { Observable } from 'rxjs';
import { Product } from '../../models/product';

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
}

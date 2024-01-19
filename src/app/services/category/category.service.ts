import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { envipronment } from '../../environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  categoryUrl = `${envipronment.apiPrefix}/categories`;
  httpHeader = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  };

  constructor(private httpClient: HttpClient) {

  }

  getCategories () : Observable<any> {
    return this.httpClient.get<any>(`${this.categoryUrl}`);
  }
}

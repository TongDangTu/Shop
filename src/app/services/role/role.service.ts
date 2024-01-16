import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { envipronment } from "../../environments/environment";
import { Observable } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class RoleService {
    private apiGetAll = `${envipronment.apiPrefix}/roles`;
    private apiConfig = {
        headers: this.createHeader()
    };
    constructor (private http:HttpClient) {
    
    }

    private createHeader () : HttpHeaders {
        return new HttpHeaders({'Content-Type' : 'application/json'});
    }

    getRoles () : Observable<any> {
        return this.http.get(this.apiGetAll, this.apiConfig);
    }

    // getRoles () : Observable<any> {
    //     return this.http.get<any[]>(this.apiGetAll);
    // }
}
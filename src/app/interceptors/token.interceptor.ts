import { Injectable } from "@angular/core";
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from "@angular/common/http";
import { Observable } from "rxjs";
import { TokenService } from "../services/token/token.service";

@Injectable()
export class TokenInterceptor implements HttpInterceptor {
    constructor (private tokenService: TokenService) {

    }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        const isToken = req.headers.get('Add-Token');
        if (isToken == "true") {
            console.log("IS HEADER ADD TOKEN");
            const token = this.tokenService.getToken();
            if (token != null) {
                req = req.clone({
                    setHeaders: {
                        Authorization: `Bearer ${token}`,
                    },
                })
            }
        }

        // const token = this.tokenService.getToken();
        //     if (token != null) {
        //         req = req.clone({
        //             setHeaders: {
        //                 Authorization: `Bearer ${token}`,
        //             },
        //         })
        //     }
        return next.handle(req);
    }
}
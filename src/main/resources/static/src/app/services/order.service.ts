import { Injectable } from '@angular/core';
import {Product} from "../dto/product";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  constructor(
    private http: HttpClient
  ) { }

  create(order: any) {
    return this.http.post(`${environment.srurl}/order/create`, order)
  }

  /*getAll() {
    return this.http.get(`${environment.srurl}/product/get/all`)
  }

  remove(id: any) {
    return this.http.delete(`${environment.srurl}/product/remove/${id}`)
  }*/

}

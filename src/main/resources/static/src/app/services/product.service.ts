import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {Product} from "../dto/product";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  type = 'phone'
  cartProducts: Product[] = []

  constructor(
    private http: HttpClient
  ) { }

  getBase64FromFile(file: File) {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => resolve(reader.result);
      reader.onerror = error => reject(error);
    });
  }
  /*
    getBase64FromFile(file).then(
      data => console.log(data)
    );
  */

  formatImage(product: Product): any {
    if (product == null || product.imageBytes == null) {
      return null
    }
    let s = product.photo
    return `data:image/${s?.substr(s.lastIndexOf('.') + 1, s.length - 1)};base64,${product.imageBytes}`;
  }

  create(product: any) {
    return this.http.post(`${environment.srurl}/product/create`, product)
  }

  getAll() {
    return this.http.get(`${environment.srurl}/product/get/all`)
  }

  getById(id: any): Observable<Product> {
    return this.http.get(`${environment.srurl}/product/get/${id}`)
  }

  remove(id: any) {
    return this.http.delete(`${environment.srurl}/product/remove/${id}`)
  }

  update(product: Product) {
    return this.http.patch(`${environment.srurl}/product/edit/${product.productId}`, product)
  }

  setType (type: string) {
    this.type = type
  }

  addProduct (product: Product) {
    let p = this.cartProducts.filter(p => p.productId == product.productId)
    if (p.length == 0) {
      this.cartProducts.push(product)
    }
  }
}

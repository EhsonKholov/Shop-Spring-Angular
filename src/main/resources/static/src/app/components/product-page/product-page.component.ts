import { Component, OnInit } from '@angular/core';
import {ProductService} from "../../services/product.service";
import {ActivatedRoute} from "@angular/router";
import {Observable, switchMap} from "rxjs";
import {Product} from "../../dto/product";

@Component({
  selector: 'app-product-page',
  templateUrl: './product-page.component.html',
  styleUrls: ['./product-page.component.css']
})
export class ProductPageComponent implements OnInit {

  product$: Observable<Product> = new Observable<Product>()

  constructor(
    public productService: ProductService,
    private router: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.product$ = this.router.params
      .pipe(switchMap(params => {
        return this.productService.getById(params['id'])
      }))
  }

  addProduct (product: Product) {
    this.productService.addProduct(product)
  }

}

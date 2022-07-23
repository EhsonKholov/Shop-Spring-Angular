import { Component, OnInit } from '@angular/core';
import {Product} from "../../../dto/product";
import {ProductService} from "../../../services/product.service";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-dashboard-page',
  templateUrl: './dashboard-page.component.html',
  styleUrls: ['./dashboard-page.component.css']
})
export class DashboardPageComponent implements OnInit {

  products: Product[] | any = [];
  pSub: Subscription = new Subscription()
  rSub: Subscription = new Subscription()
  productName: string = ''

  constructor(
    private productService: ProductService
  ) { }

  ngOnInit(): void {
    this.pSub = this.productService.getAll()
      .subscribe(products => {
          return this.products = products;
        }
      )
  }

  ngOnDestroy() {
    if (this.pSub) {
      this.pSub.unsubscribe()
    }
    if (this.rSub) {
      this.rSub.unsubscribe()
    }
  }

  remove(productId: any) {
    this.rSub = this.productService.remove(productId)
      .subscribe(() => {
        this.products = this.products.filter((product: Product) => product.productId !== productId)
      })
  }
}

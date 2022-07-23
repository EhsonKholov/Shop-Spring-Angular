import { Component, OnInit } from '@angular/core';
import {Product} from "../../dto/product";
import {ProductService} from "../../services/product.service";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {OrderService} from "../../services/order.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-cart-page',
  templateUrl: './cart-page.component.html',
  styleUrls: ['./cart-page.component.css']
})
export class CartPageComponent implements OnInit {

  cartProducts: Product[] = []
  totalPrice = 0
  form: any
  submitted = false

  constructor(
    private productService: ProductService,
    private orderService: OrderService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.calculation()

    this.form = new FormGroup({
      name: new FormControl(null, Validators.required),
      phone: new FormControl(null, Validators.required),
      address: new FormControl(null, Validators.required),
      payment: new FormControl('Cache'),
    })
  }

  calculation () {
    this.cartProducts = this.productService.cartProducts
    this.totalPrice = 0
    // @ts-ignore
    this.cartProducts.forEach((p: Product) => this.totalPrice += p.price)
  }


  deleteElement (cartProduct: Product) {
    let idx = this.productService.cartProducts.indexOf(cartProduct)
    if (idx > -1) {
      this.productService.cartProducts.splice(idx, 1)
    }
    this.calculation()
  }

  submit() {
    if (this.form.invalid) {
      return
    }
    //element.files[0].name = element.files[0].name.replace(/^.*[\\\/]/, '');
    //this.submitted = true

    this.cartProducts = this.cartProducts.map(p => {
      return {
        ...p,
        imageBytes: ''
      }
    })

    const order = {
      name: this.form.value.name,
      phone: this.form.value.phone,
      address: this.form.value.address,
      payment: this.form.value.info,
      products: this.cartProducts,
      price: this.totalPrice,
      created: new Date()
    }

    this.orderService.create(order).subscribe((res) => {
      this.form.reset()
      this.submitted = false
      this.cartProducts = []
      this.router.navigate(['/'])
    }, (error) => {
      this.submitted = false
    });
  }

}

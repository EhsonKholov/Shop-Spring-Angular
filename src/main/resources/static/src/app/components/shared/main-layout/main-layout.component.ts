import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {ProductService} from "../../../services/product.service";

@Component({
  selector: 'app-main-layout',
  templateUrl: './main-layout.component.html',
  styleUrls: ['./main-layout.component.css']
})
export class MainLayoutComponent implements OnInit {

  type = 'phone'
  constructor(
    private router: Router,
    private productService: ProductService
  ) { }

  ngOnInit(): void {
  }

  setType (type: string) {
    this.type = type

    if (this.type != 'Cart') {
      this.router.navigate(['/'], {
        queryParams: {
          type: this.type
        }
      })
    }

    this.productService.setType(this.type)
  }

}

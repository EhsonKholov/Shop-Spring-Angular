import {Component, Input, OnInit} from '@angular/core';
import {ProductService} from "../../services/product.service";
import {Product} from "../../dto/product";

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css']
})
export class ProductComponent implements OnInit {

  @Input() product: any;

  constructor(public productService: ProductService) { }

  ngOnInit(): void {
  }

  addProduct (product: Product) {
    this.productService.addProduct(product)
  }

}

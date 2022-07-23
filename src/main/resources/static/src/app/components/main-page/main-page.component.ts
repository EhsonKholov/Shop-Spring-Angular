import { Component, OnInit } from '@angular/core';
import {ProductService} from "../../services/product.service";
import {Observable} from "rxjs";
import {tap} from "rxjs/operators";

@Component({
  selector: 'app-main-page',
  templateUrl: './main-page.component.html',
  styleUrls: ['./main-page.component.css']
})
export class MainPageComponent implements OnInit {

  products$: Observable<any> | null = null;

  constructor(
    public productService: ProductService
  ) { }

  ngOnInit(): void {
    this.products$ = this.productService.getAll()
  }



}

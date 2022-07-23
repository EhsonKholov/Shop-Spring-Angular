import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {ProductService} from "../../../services/product.service";
import {switchMap} from "rxjs";
import {Product} from "../../../dto/product";
import {FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-edit-page',
  templateUrl: './edit-page.component.html',
  styleUrls: ['./edit-page.component.css']
})
export class EditPageComponent implements OnInit {

  form: any //FormGroup = new FormGroup({})
  product: Product = {}
  file: File = new File([], '')
  submitted = false

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    public productService: ProductService
  ) { }

  ngOnInit(): void {
    this.activatedRoute.params.pipe(
      switchMap(params => {
        return this.productService.getById(params['id'])
      })
    ).subscribe(product => {
      this.product = product
      this.form = new FormGroup({
        type: new FormControl(product.type, Validators.required),
        title: new FormControl(product.title, Validators.required),
        photo: new FormControl(product.photo, Validators.required),
        //imageBytes: new FormControl(product.imageBytes, Validators.required),
        info: new FormControl(product.info, Validators.required),
        price: new FormControl(product.price, Validators.required),
      })
    })
  }

  submit() {
    if (this.form.invalid)
      return

    this.submitted = true

    this.productService.update({
        ...this.product,
        type: this.form.value.type,
        title: this.form.value.title,
        info: this.form.value.info,
        price: this.form.value.price,
        updated: new Date()
    }).subscribe(res => {
      this.submitted = false
      this.router.navigate(['/admin', 'dashboard'])
    }, error => {
      this.submitted = false
    })

  }

  onFileSelected ($event: any) {
    if ($event.target.files.length > 0) {
      this.file = $event.target.files[0]
      this.productService.getBase64FromFile($event.target.files[0]).then(
        (data: any) => {
          this.product.imageBytes = data?.substr(data?.indexOf('base64,')+'base64,'.length)
          this.product.photo = this.file.name
          this.form.value.imageBytes = this.product.imageBytes
          this.form.value.photo = this.product.photo
        }
      );
    }
  }


}

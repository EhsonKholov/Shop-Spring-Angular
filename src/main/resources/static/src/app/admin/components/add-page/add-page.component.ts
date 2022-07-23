import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {ProductService} from "../../../services/product.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-add-page',
  templateUrl: './add-page.component.html',
  styleUrls: ['./add-page.component.css']
})
export class AddPageComponent implements OnInit {

  form?: any;
  submitted = false;
  file: any;

  constructor(
    private productService: ProductService,
    private router: Router
    ) { }

  ngOnInit(): void {
    this.form = new FormGroup({
      type: new FormControl(null, Validators.required),
      title: new FormControl(null, Validators.required),
      photo: new FormControl(null, Validators.required),
      info: new FormControl(null, Validators.required),
      price: new FormControl(null, Validators.required),
    })
  }

  onFileSelected ($event: any) {
    if ($event.target.files.length > 0) {
      this.file = $event.target.files[0];
    }
  }

  submit() {
    if (this.form.invalid) {
      return
    }
    //element.files[0].name = element.files[0].name.replace(/^.*[\\\/]/, '');
    //this.submitted = true

    const product = {
      type: this.form.value.type,
      title: this.form.value.title,
      photo: this.form.value.photo,
      info: this.form.value.info,
      price: this.form.value.price,
      created: new Date()
    }

    const formData: FormData = new FormData();
    formData.append('product', JSON.stringify(product))
    formData.append('file', this.file, this.file.name)

    this.productService.create(formData).subscribe((res) => {
      this.form.reset()
      this.submitted = false
      this.router.navigate(['/'])
    }, (error) => {
      this.submitted = false
    });
  }

}

import { Pipe, PipeTransform } from '@angular/core';
import {Product} from "../dto/product";

@Pipe({
  name: 'search'
})
export class SearchPipe implements PipeTransform {

  transform(products: Product[], productName: string = ''): any {
    if (!productName.trim()) {
      return products
    }

    return products.filter((product: Product) => {
      return product.title?.toLowerCase().includes(productName.toLowerCase())
    })
  }

}

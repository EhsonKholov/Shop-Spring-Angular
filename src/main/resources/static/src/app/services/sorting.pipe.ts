import { Pipe, PipeTransform } from '@angular/core';
import {Product} from "../dto/product";

@Pipe({
  name: 'sorting'
})
export class SortingPipe implements PipeTransform {

  transform(products: Product[], type: string = ''): any {
    return products.filter((product: Product) => {
      return product.type == type
    })
  }

}

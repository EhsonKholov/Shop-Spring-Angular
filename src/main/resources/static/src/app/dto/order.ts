import {Product} from "./product";

export interface Order {
  orderId?: string
  name?: string
  phone?: string
  address?: string
  payment?: string
  products?: Product
  price?: number
  created?: Date
}

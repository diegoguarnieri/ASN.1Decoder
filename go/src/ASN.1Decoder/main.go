package main

import (
   "fmt"
   "ASN.1Decoder/utils"
   "ASN.1Decoder/types"
   "ASN.1Decoder/lib"
)

func main() {
   
   file := "/mnt/hgfs/shared/b00000001.dat"

   rawData := make(map[int]types.Ber)

   err := utils.LoadBER(file, rawData)
   if err != nil {
      fmt.Println(err)
   } else {

      fmt.Println("File loaded")

      asn, err := lib.Decode(rawData);
      if err != nil {
         fmt.Println(err)
      } else {
         fmt.Println("File decoded")

         lib.PrintASN(asn, 0)
      }
   }
}

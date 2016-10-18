package lib

import (
   "fmt"
   "strconv"
   "strings"
   "ASN.1Decoder/types"
   "ASN.1Decoder/utils"
)

func leftPad(s string, padStr string, pLen int) string {
   return strings.Repeat(padStr, pLen) + s
}

func PrintASN(asn types.ASN, ident int) {
   class := utils.GetClass(asn.Class)
   classType := utils.GetClassType(asn.ClassType)
   dataType, _ := strconv.ParseInt(asn.DataType, 2, 32)
   length := asn.Length
   value := asn.Value
   pad := leftPad("", " ", 3 * ident)

   //fmt.Printf("%+v\n",asn)
   //fmt.Printf("Seq: %d Class: %s ClassType: %s DataType: %d Length: %d Value: %s\n",
   fmt.Printf("%sClass: %s ClassType: %s DataType: %d Length: %d Value: %s\n",
      pad,
      class,
      classType,
      dataType,
      length,
      value)

   for _, asnChildren := range asn.Children {
      PrintASN(asnChildren, ident + 1)
   }
}

func PrintASN2(asn types.ASN, ident int) {
   class := utils.GetClass(asn.Class)
   classType := utils.GetClassType(asn.ClassType)
   dataType, _ := strconv.ParseInt(asn.DataType, 2, 32)
   value := asn.Value
   pad := leftPad("", " ", 3 * ident)

   //fmt.Printf("%+v\n",asn)
   //fmt.Printf("Seq: %d Class: %s ClassType: %s DataType: %d Length: %d Value: %s\n",
   fmt.Printf("%sC->%s T->%s TD->%d V->%s\n",
      pad,
      class,
      classType,
      dataType,
      value)

   for _, asnChildren := range asn.Children {
      PrintASN2(asnChildren, ident + 1)
   }
}

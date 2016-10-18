package lib

import (
   "fmt"
   "strconv"
   //"runtime/debug"
   "ASN.1Decoder/types"
   "ASN.1Decoder/utils"
)

var data map[int]types.Ber

func Decode(rawData map[int]types.Ber) (types.ASN, error) {

   //init array to prevent 'assignment to entry in nil map'
   //asn.array = make(map[int]types.ASN)

   data = rawData

   asn := types.ASN{}

   octets := 0
   for octets < len(data) {
      asnChildren := types.ASN{}

      o, err := d("T", octets, &asnChildren, 0)
      if err != nil {
         fmt.Println("Error 29", err)
         break
      } else {
         asn.Children = append(asn.Children, asnChildren)
         octets += o
      }
   }
   
   return asn, nil
}

func d(tlv string, index int, asn *types.ASN, length int) (int, error) {

   elem, ok := data[index]
   if ok == false {
      return 0, &types.NoDataFound {}
   }
   raw := elem.Raw

   if tlv == "T" {
      //TAG
      //fmt.Println(index, "TAG")

      asn.Class = utils.BinarySubstring(raw,0,2)
      asn.ClassType = utils.BinarySubstring(raw,2,1)

      octets := 1
      dataType := utils.BinarySubstring(raw,3,5)
      if dataType == "11111" {
         
         dataType = ""
         for {
            index++
            octets++

            elem, ok = data[index]
            if ok == false {
               return 0, &types.NoDataFound {}
            }
            raw = elem.Raw

            dataType += utils.BinarySubstring(raw,1,7)

            //repet until the first position equal 0
            if strconv.Itoa(int(utils.GetBinary(raw,0))) == "0" {
               break
            }
         }
      }
      asn.DataType = dataType

      o, err := d("L", index + 1, asn, length)
      if err != nil {
         fmt.Println("Error 84", err)
         return 0, &types.Abort {err}
      }

      return octets + o, nil

   } else if tlv == "L" {
      //LENGTH
      //fmt.Println(index, "LENGTH")

      stringLength := utils.BinarySubstring(raw,1,7)      
      newLength, err := strconv.ParseInt(stringLength, 2, 32)
      if err != nil {
         //error
         fmt.Println("Error 98", err)
         return 0, &types.Abort {err}
      }

      octets := 1
      if strconv.Itoa(int(utils.GetBinary(raw,0))) == "1" {
         //length more to 128 octets
         stringLength = ""
         for i := index + 1; i <= index + int(newLength); i++ {
            octets++

            elem, ok = data[i]
            if ok == false {
               return 0, &types.NoDataFound {}
            }
            raw = elem.Raw

            stringLength += utils.BinarySubstring(raw,0,8)
         }

         newLength, err = strconv.ParseInt(stringLength, 2, 32)
         if err != nil {
            //error
            fmt.Println("Error 121", err)
            return 0, &types.Abort {err}
         }
      }
      asn.Length = int(newLength)

      if newLength != 0 {
         o, err := d("V", index + octets, asn, int(newLength))
         if err != nil {
            fmt.Println("Error 129", err)
            return 0, &types.Abort {err}
         }

         return octets + o, nil
      } else {
         return octets, nil
      }

   } else {
      //VALUE
      //fmt.Println(index, "VALUE")

      if asn.ClassType == "0" {
         //0 - primitive

         stringValue := ""
         for i := index; i < index + length; i++ {
            elem, ok = data[i]
            if ok == false {
               fmt.Println("Error 145")
               return 0, &types.NoDataFound {}
            }
            raw = elem.Raw

            stringValue += utils.BinarySubstring(raw,0,8)
         }
         asn.Value = stringValue

         return length, nil
         
      } else {
         //1 - constructed
         octets := 0
         for octets < length {
            asnChildren := types.ASN{}

            o, err := d("T", index + octets, &asnChildren, length)
            if err != nil {
               fmt.Println("Error 164", err)
               return 0, &types.Abort {err}
            }
            octets += o
            asn.Children = append(asn.Children, asnChildren)
         }

         return octets, nil
      }
   }
}

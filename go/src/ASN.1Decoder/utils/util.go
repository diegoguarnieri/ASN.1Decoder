package utils

import "strconv"

//swap array position -> i=0 pos=7
func GetBinary(raw uint8, i uint) uint8 {
   i = 7 - i
   return (raw & (1 << i) >> i)
}

func BinarySubstring(raw uint8, init uint, length int) string {
   var s string = ""
   l := 1
   for i := init; l <= length; i++ {
      s += strconv.Itoa(int(GetBinary(raw,i)))
      l++
   }

   return s
}

func GetClass(class string) string {
   switch class {
   case "00":
      return "universal"
   case "01":
      return "application"
   case "10":
      return "context-especific"
   case "11":
      return "private"
   default:
      return ""
   }
}

func GetClassType(classType string) string {
   switch classType {
   case "0":
      return "primitive"
   case "1":
      return "constructed"
   default:
      return ""
   }
}
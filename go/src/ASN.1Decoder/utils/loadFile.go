package utils

import (
   "io"
   "os"
   "ASN.1Decoder/types"
)

func LoadBER(fileName string, data map[int]types.Ber) error {

   file, errOpenFile := os.Open(fileName)

   if errOpenFile == nil {
      b := make([]byte, 1)
      i := 0
      for {
         line, errReadFile := file.Read(b)

         if errReadFile == io.EOF {
            break
         }
         
         data[i] = types.Ber{b[:line][0]}
         i++
      }

      return nil

   } else {
      return &types.CustomError{"Couldn't open file"}
   }
}

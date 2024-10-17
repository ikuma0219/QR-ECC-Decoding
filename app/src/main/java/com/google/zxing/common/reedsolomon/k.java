// package com.google.zxing.common.reedsolomon;

// public class k {
//     public int decodeWithECCount(int[] received, int twoS) throws ReedSolomonException {
//         GenericGFPoly poly = new GenericGFPoly(field, received);
//         int[] syndromeCoefficients = new int[twoS];
//         boolean noError = true;
//         for (int i = 0; i < twoS; i++) {
//           int eval = poly.evaluateAt(field.exp(i + field.getGeneratorBase()));
//           syndromeCoefficients[syndromeCoefficients.length - 1 - i] = eval;
//           if (eval != 0) {
//             noError = false;
//           }
//         }
//         if (noError) {
//           return 0;
//         }
//         GenericGFPoly syndrome = new GenericGFPoly(field, syndromeCoefficients);
//         GenericGFPoly[] sigmaOmega =
//             runEuclideanAlgorithm(field.buildMonomial(twoS, 1), syndrome, twoS);
//         GenericGFPoly sigma = sigmaOmega[0];
//         GenericGFPoly omega = sigmaOmega[1];
//         int[] errorLocations = findErrorLocations(sigma);
//         int[] errorMagnitudes = findErrorMagnitudes(omega, errorLocations);
//         for (int i = 0; i < errorLocations.length; i++) {
//           int position = received.length - 1 - field.log(errorLocations[i]);
//           if (position < 0) {
//             throw new ReedSolomonException("Bad error location");
//           }
//           received[position] = GenericGF.addOrSubtract(received[position], errorMagnitudes[i]);
//         }
//         return errorLocations.length;
//       }
   
    
// }

//   ///// 消失訂正
//   public int erasedecodeWithECCount(int[] received, int[] erasePositions, int twoS) throws ReedSolomonException {
//     GenericGFPoly poly = new GenericGFPoly(field, received);
//      System.out.println(Arrays.toString(received));

//     // シンドロームの計算
//     int[] syndromeCoefficients = new int[twoS];
//     boolean noError = true;
//     for (int i = 0; i < twoS; i++) {
//         int eval = poly.evaluateAt(field.exp(i + field.getGeneratorBase()));
//         syndromeCoefficients[syndromeCoefficients.length - 1 - i] = eval;
//         if (eval != 0) {
//             noError = false;
//         }
//     }
//     if (noError) {
//         return 0;
//     }
//     GenericGFPoly syndrome = new GenericGFPoly(field, syndromeCoefficients);

//    // 消失位置多項式λ(x)の構成
//     int eraseNum = erasePositions.length;
//     GenericGFPoly lambda = new GenericGFPoly(field, new int[]{1});
//     for (int erasePosition : erasePositions) {
//         int reversePos = received.length - 1 - erasePosition;
//         GenericGFPoly factor = new GenericGFPoly(field, new int[]{field.exp(reversePos), 1});
//         lambda = lambda.multiply(factor);
//     }

//     // S(x) × λ(x)を計算し、2t次以上の項を切り捨て
//     GenericGFPoly sLambda = syndrome.multiply(lambda);
//     sLambda = truncatePolynomial(sLambda, syndromeCoefficients.length);

//     // ユークリッドのアルゴリズムで誤り位置多項式σ(x)と誤り評価多項式ψ(x)を求める
//     GenericGFPoly[] sigmaPsi = runEuclideanAlgorithm(field.buildMonomial(twoS, 1), sLambda, twoS + eraseNum);
//     GenericGFPoly sigma = sigmaPsi[0];
//     GenericGFPoly psi = sigmaPsi[1];

//     // 誤り位置と消失位置を探す
//     int[] errorLocations = findErrorLocations(sigma);
//     int[] eraseLocations = findErrorLocations(lambda);

//     // 誤りの大きさと消失の大きさを計算
//     int[] errorMagnitudes = calculateMagnitudes(psi, errorLocations, lambda);
//     int[] eraseMagnitudes = calculateMagnitudes(psi, eraseLocations, sigma);

//     // 誤りと消失の訂正
//     correctErrors(received, errorLocations, errorMagnitudes);
//     correctErrors(received, eraseLocations, eraseMagnitudes);

//       // 訂正結果を確認
//     if (!verifyCorrection(received, twoS)) {
//         throw new ReedSolomonException("Bad error location");
//     }

//     return errorLocations.length;
// }

// // Helper function 多項式を2t次で切り捨てる
// private GenericGFPoly truncatePolynomial(GenericGFPoly poly, int degree) {
//     if (poly.getDegree() < degree - 1) {
//         return poly;
//     }
//     int[] coefficients = new int[degree];
//     for (int i = 0; i < degree; i++) {
//         coefficients[i] = poly.getCoefficient(degree - 1 - i);
//     }
//     return new GenericGFPoly(field, coefficients);
// }

// // Helper function 誤りや消失の大きさを計算
// private int[] calculateMagnitudes(GenericGFPoly psi, int[] locations, GenericGFPoly locatorPoly) throws ReedSolomonException {
//     int[] magnitudes = findErrorMagnitudes(psi, locations);
//     for (int i = 0; i < magnitudes.length; i++) {
//         int inverseLoc = locatorPoly.evaluateAt(field.inverse(locations[i]));
//         magnitudes[i] = field.divide(inverseLoc, magnitudes[i]);
//     }
//     return magnitudes;
// }

// // Helper function 受信したデータに対して誤りや消失を訂正
// private void correctErrors(int[] received, int[] locations, int[] magnitudes) throws ReedSolomonException {
//     for (int i = 0; i < locations.length; i++) {
//         int pos = received.length - 1 - field.log(locations[i]);
//         if (pos < 0) {
//             throw new ReedSolomonException("Bad error location");
//         }
//         received[pos] = GenericGF.addOrSubtract(received[pos], magnitudes[i]);
//     }
// }

// // Helper function 訂正結果が正しいかを確認
// private boolean verifyCorrection(int[] received, int twoS) {
//     GenericGFPoly poly = new GenericGFPoly(field, received);
//     for (int i = 0; i < twoS; i++) {
//         int eval = poly.evaluateAt(field.exp(i + field.getGeneratorBase()));
//         if (eval != 0) {
//             return false;
//         }
//     }
//     return true;
// }

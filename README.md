# Product-Service

## 5. 라이브러리 사용 이유

- Querydsl
    - JPQL의 컴파일 시점 오류 확인 불가, 쿼리 가독성과 타입 안정성을 보장하여 유지보수를 용이하게 하기위해 사용

## 7. 브랜치 및 디렉토리 구조

> 디렉토리 구조

![directory_structure_product](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdna%2Fbt3DBS%2FbtsPYNYDHku%2FAAAAAAAAAAAAAAAAAAAAAME3BC2QgCGwb4NFkaS2lKaoVr_Dktz8DoaAWSidXjqG%2Fimg.png%3Fcredential%3DyqXZFxpELC7KVnFOS48ylbz2pIh7yKj8%26expires%3D1756652399%26allow_ip%3D%26allow_referer%3D%26signature%3DYZZyl%252FtdBHZ7T%252FRXNeWgIFzOqUk%253D)

## 8. 주요 기능

| 기능                      | 메서드    | 엔드포인트                           | Query Params                  | status |
|-------------------------|--------|---------------------------------|-------------------------------|--------|
| 상품 생성                   | POST   | /products                       |                               | 201    |
| 상품 단건 조회                | GET    | /products/{productId}           |                               | 200    |
| 상품 전체 목록 조회 (페이지네이션)    | GET    | /products                       | page, size, sort, productName | 200    |
| 허브 등록 상품 목록 조회 (페이지네이션) | GET    | /products/hubs/{hubId}          | page, size, sort, productName | 200    |
| 업체 등록 상품 목록 조회 (페이지네이션) | GET    | /products/companies/{companyId} | page, size, sort, productName | 200    |
| 상품 수정                   | PATCH  | /products                       |                               | 204    |
| 상품 삭제                   | DELETE | /products                       |                               | 200    |

## 9. 상세 담당 업무

### Product

- 유저 권한별 각 기능 유효성 검증
    <details>
        <summary>
            상품 생성
        </summary>

  ![상품_생성](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdna%2FbkOHf3%2FbtsP418xBfU%2FAAAAAAAAAAAAAAAAAAAAANTAYV6Flmf0aYQFKiT4bxZ1rYOAtrKdJiaVmSiT-Djf%2Fimg.png%3Fcredential%3DyqXZFxpELC7KVnFOS48ylbz2pIh7yKj8%26expires%3D1756652399%26allow_ip%3D%26allow_referer%3D%26signature%3DHfO4qIDI98YUiZOjXkxT9a%252FovSc%253D)

    </details>
    <details>
        <summary>
            상품 단건 조회
        </summary>

  ![상품_단건_조회](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdna%2FcENDh5%2FbtsP0TdqBr5%2FAAAAAAAAAAAAAAAAAAAAANf3fNevha4wHkfxJJ67djEjfy5lv1rKUe3KXfPKcgpX%2Fimg.png%3Fcredential%3DyqXZFxpELC7KVnFOS48ylbz2pIh7yKj8%26expires%3D1756652399%26allow_ip%3D%26allow_referer%3D%26signature%3D3U%252FQRnYrfBsrAEKrd0mYSjTZ5bU%253D)

    </details>
    <details>
        <summary>
            허브 등록 상품 목록 조회
        </summary>

  ![허브_등록_상품_목록_조회](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdna%2Fb4gF5b%2FbtsP4e1BM6F%2FAAAAAAAAAAAAAAAAAAAAAK6eFEKK3E7u75yOHEyw7wyeLkgoXhoURZRjijhMSXAu%2Fimg.png%3Fcredential%3DyqXZFxpELC7KVnFOS48ylbz2pIh7yKj8%26expires%3D1756652399%26allow_ip%3D%26allow_referer%3D%26signature%3DiF7Tes%252FSHgq6KK4yk8skVfxcetU%253D)

    </details>
    <details>
        <summary>
            업체 등록 상품 목록 조회
        </summary>

  ![업체_등록_상품_목록_조회](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdna%2FcBTWKX%2FbtsP4qnocyX%2FAAAAAAAAAAAAAAAAAAAAAB0R89YUWV3XmIA38WrNvcG-_dHulc9O1Eb-FpLAxjyY%2Fimg.png%3Fcredential%3DyqXZFxpELC7KVnFOS48ylbz2pIh7yKj8%26expires%3D1756652399%26allow_ip%3D%26allow_referer%3D%26signature%3DGajD4ta0IdFS47DK2W%252BrkfmgUK4%253D)

    </details>
    <details>
        <summary>
            상품 수정
        </summary>

  ![상품_수정](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdna%2FxCLnK%2FbtsP4624ifa%2FAAAAAAAAAAAAAAAAAAAAADOQQIn41TP2pwZ0BqZHCZWjLL1KsOUAGAr106eHNrTi%2Fimg.png%3Fcredential%3DyqXZFxpELC7KVnFOS48ylbz2pIh7yKj8%26expires%3D1756652399%26allow_ip%3D%26allow_referer%3D%26signature%3DmT3cGgJRQjY1AKMHSh2Xe6G8poI%253D)

    </details>
    <details>
        <summary>
            상품 삭제
        </summary>

  ![상품_삭제](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdna%2Fb11bVy%2FbtsP4iQtGIB%2FAAAAAAAAAAAAAAAAAAAAAI8faBKIc_q6REmCQGIDYSxiT_pvo8IPVVOMuqHk75wi%2Fimg.png%3Fcredential%3DyqXZFxpELC7KVnFOS48ylbz2pIh7yKj8%26expires%3D1756652399%26allow_ip%3D%26allow_referer%3D%26signature%3DF%252FQTJXHI3PKAvxOUBfWsQ8drl0A%253D)

    </details>


- DTO의 `record` 타입 사용
    - 클라이언트의 요청에 대한 불변성과 안정성을 명시

 
- `Pageable`의 `sort` 쿼리 - 정렬 조건 타입 안정화
    - 정해진 정렬 조건만 허용 - 예기치 못한 쿼리 또는 에러 발생 방지
    - `SortType` ENUM
      ```
      @Getter
      @RequiredArgsConstructor
      public enum SortType {
      
          CREATED_ASC("createdAt", Sort.Direction.ASC),
          CREATED_DESC("createdAt", Sort.Direction.DESC),
          UPDATED_ASC("updatedAt", Sort.Direction.ASC),
          UPDATED_DESC("updatedAt", Sort.Direction.DESC),
          PRICE_ASC("orderPrice", Sort.Direction.ASC),
          PRICE_DESC("orderPrice", Sort.Direction.DESC),
          ;
      
          private final String value;
          private final Sort.Direction direction;
      
          public static void validate(Sort sort) {
              for (Sort.Order order : sort) {
                  boolean valid = Arrays.stream(SortType.values())
                          .anyMatch(sortType -> sortType.value.equalsIgnoreCase(order.getProperty()) &&
                                  sortType.getDirection().equals(order.getDirection()));
      
                  if (!valid) {
                      throw new CustomRuntimeException(ProductException.UNSUPPORTED_SORT_TYPE);
                  }
              }
          }
      }
      ```

- `Querydsl`의 `getOrderSpecifier()`을 통한 동적 정렬 조건 쿼리 구현
    - 클라이언트가 여러 개의 `sort` 쿼리를 넘길 수 있도록 하여 `.orderBy()`에 적용
      ```
      private OrderSpecifier<?>[] getOrderSpecifiers(Sort sort) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
    
        for (Sort.Order sortOrder : sort) {
            Order order = sortOrder.isAscending() ? Order.ASC : Order.DESC; // 정렬 방향 
            PathBuilder<Product> pathBuilder
                    = new PathBuilder<>(product.getType(), product.getMetadata());
    
            orderSpecifiers.add(new OrderSpecifier<>(order, pathBuilder.getString(sortOrder.getProperty())));
        }
    
        return orderSpecifiers.toArray(new OrderSpecifier[0]);
      }
      ```
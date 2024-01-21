//
//  TiLinesViewProxy.h
//  ti.lines
//
//  Created by miga on 21.01.24.
//

#import "TiUIView.h"

@interface TiLinesView : TiUIView {
    UIView *square;
    NSInteger startAt;
    NSInteger lineWidth;
    NSInteger maxValue;
    TiColor *lineColor;
    NSArray *values;
}
@end

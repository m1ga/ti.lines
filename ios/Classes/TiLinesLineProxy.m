//
//  TiLinesViewProxy.m
//  ti.lines
//
//  Created by miga on 21.01.24.
//

#import "TiLinesLineProxy.h"

@implementation TiLinesLineProxy

- (void)redraw:(id)args
{
    [(TiLinesLineProxy *)[self view] redraw:args];
}

- (void)clear:(id)args
{
    [(TiLinesLineProxy *)[self view] clear:args];
}

@end
